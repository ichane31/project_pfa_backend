package com.example.pfa_backend.services;

import com.example.pfa_backend.email.context.AccountVerificationEmailContext;
import com.example.pfa_backend.email.context.ForgotPasswordEmailContext;
import com.example.pfa_backend.email.service.EmailServiceConf;
import com.example.pfa_backend.exception.InvalidTokenException;
import com.example.pfa_backend.exception.InvalidUserDataException;
import com.example.pfa_backend.exception.NotFoundException;
import com.example.pfa_backend.models.ResetPasswordToken;
import com.example.pfa_backend.models.User;
import com.example.pfa_backend.payload.request.ChangePasswordDto;
import com.example.pfa_backend.payload.request.LoginRequest;
import com.example.pfa_backend.payload.request.ResetPassword;
import com.example.pfa_backend.payload.response.UserInfoResponse;
import com.example.pfa_backend.repositories.RoleRepository;
import com.example.pfa_backend.repositories.UserRepository;
import com.example.pfa_backend.security.Services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private ResetPasswordTokenService resetPasswordTokenService;

    @Autowired
    private EmailServiceConf emailServiceConf;

    @Value("${site.changepassword.url.https}")
    private String frontUrl;

    public List<User> getAllUsers() {
        List<User> users =userRepository.findAll();
        return users;
    }

    public User getUserbyID(int id) throws NotFoundException {
        List<User> users = userRepository.findAll();
        User user = null;
        for(User con : users) {
            if(con.getId()==id) {
                user=con;
            }
        }
        if(user ==null) {
            throw new NotFoundException(String.format("User not found for id =%s" ,id));
        }
        return user;
    }

    public User getUserbyEmal(String email) throws NotFoundException {
        List<User> users = userRepository.findAll();
        User user = null;
        if(email==null) {
            throw new InvalidUserDataException("Email cannot be null");
        }
        for(User con : users) {
            if(con.getEmail().equalsIgnoreCase(email)) {
                user=con;
            }
        }
        if(user ==null) {
            throw new NotFoundException(String.format("User not found for email =%s" ,email));
        }
        return user;
    }

    public void checkIfEmailNotUsed(String email) throws NotFoundException {
        User userByEmail = getUserbyEmal(email);
        if (userByEmail != null) {
            String msg = String.format("The email %s it's already in use from another user");
            throw new InvalidUserDataException(String.format("This email %s it's already in use.",
                    userByEmail.getEmail()));
        }
    }

    public void sendRegistrationConfirmationEmail22(User user ,String password) {
        AccountVerificationEmailContext emailContext = new AccountVerificationEmailContext();
        emailContext.init(user, password);
        try {
            emailServiceConf.sendMail(emailContext);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendResetPasswordEmail(User user) {
        ResetPasswordToken resetPasswordToken = resetPasswordTokenService.create(user);
        ForgotPasswordEmailContext forgotPasswordEmailContext = new ForgotPasswordEmailContext();
        forgotPasswordEmailContext.init(user);
        forgotPasswordEmailContext.setToken(resetPasswordToken.getToken());
        forgotPasswordEmailContext.buildVerificationUrl(frontUrl,resetPasswordToken.getToken());
        try {
            emailServiceConf.sendMail(forgotPasswordEmailContext);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void resetPassword(String token, ResetPassword resetPassword) throws InvalidTokenException {
        // Recherche de l'utilisateur associé au token
        User user = userRepository.findByPasswordToken(token);

        // Vérification de la validité du token
        if (user == null || user.getPassTokenExpiryTime().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Invalid or expired token");
        }

        // Réinitialisation du mot de passe
        user.setPassword(encoder.encode(resetPassword.getNewPassword()));
        user.setPasswordToken(null);
        user.setPassTokenExpiryTime(null);
        userRepository.save(user);
    }

    public void activateUser (int id) throws NotFoundException {
        User user = getUserbyID(id);
        user.setAccountVerified(true);
        userRepository.save(user);
    }
    public UserInfoResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();


        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new UserInfoResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void incrementFailedLoginAttempts(User user) {
        int currentAttempts = user.getFailedLoginAttempts();
        user.setFailedLoginAttempts(currentAttempts + 1);

        if (currentAttempts + 1 >= 3) {
            user.setLoginDisabled(true);
        }

        userRepository.save(user);
    }

    public void resetFailedLoginAttempts(User user) {
        user.setFailedLoginAttempts(0);
        user.setLoginDisabled(false);
        userRepository.save(user);
    }

    public void changePassword(String email, ChangePasswordDto changePasswordDto) throws InvalidUserDataException, NotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        String oldPassword = changePasswordDto.getOldPassword();
        String newPassword = changePasswordDto.getNewPassword();
        String repeatPassword = changePasswordDto.getRepeatPassword();

        if (!newPassword.equals(repeatPassword)) {
            throw new InvalidUserDataException("New passwords don't match");
        }

        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidUserDataException("Incorrect old password");
        }

        String encodedPassword = encoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }
}
