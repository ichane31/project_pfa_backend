package com.example.pfa_authentification.controllers;

import com.example.pfa_authentification.exception.NotFoundException;
import com.example.pfa_authentification.models.ERole;
import com.example.pfa_authentification.models.Role;
import com.example.pfa_authentification.models.User;
import com.example.pfa_authentification.payload.request.*;
import com.example.pfa_authentification.payload.response.LoginResponse;
import com.example.pfa_authentification.repositories.RoleRepository;
import com.example.pfa_authentification.repositories.UserRepository;
import com.example.pfa_authentification.security.Services.UserDetailsImpl;
import com.example.pfa_authentification.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class UserControllers {

    @Autowired
    AuthenticationManager authenticationManager;

    private static final String REDIRECT_LOGIN= "redirect:http://localhost:3000/login";

    @Resource
    private MessageSource messageSource;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserService userService;

    //@ApiOperation(value = "Get a resource by ID", response = Resource.class)
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.getUserbyEmal(loginRequest.getEmail());

            if(user!=null) {
                if (user.isLoginDisabled()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Your account has been disabled due to too many failed login attempts");
                }
                if(!user.isAccountVerified()) {
                    Map<Object, Object> model = new HashMap<>();
                    model.put("message","Veuillez consulter votre compte e-mail afin de complete votre inscription!!!!!");
                    return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);
                }
                if(!encoder.matches(loginRequest.getPassword(),user.getPassword())) {
                    userService.incrementFailedLoginAttempts(user);
                    Map<Object, Object> model = new HashMap<>();
                    model.put("message","Incorrect Password !!!!!");

                    return new ResponseEntity<>(model,HttpStatus.BAD_REQUEST);
                }

                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                List<String> roles = userDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(toList());
                userService.resetFailedLoginAttempts(user);
                return ok(new LoginResponse(
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getLastName(),
                        userDetails.getEmail(),
                        userDetails.getPassword(),
                        roles));
            }

            Map<Object, Object> model = new HashMap<>();
            model.put("message","User not found !!!!!");

            return new ResponseEntity<>(model,HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            Map<Object, Object> model = new HashMap<>();
            model.put("message",e.getMessage());

            return new ResponseEntity<>(model,HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest signUpRequest) {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body("error: email is already in use!");
        }

        // Create new user's account
        User user = new User(signUpRequest.getFirstName(),signUpRequest.getLastName() ,signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_PATIENT)
                    .orElseThrow(() -> new RuntimeException("error: role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("error: role is not found."));
                        roles.add(adminRole);
                        user.setAdmin(1);

                        break;
                    case "medecin":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MEDECIN)
                                .orElseThrow(() -> new RuntimeException("error: role is not found."));
                        roles.add(modRole);

                        break;
                    case "patient":
                        Role patRole = roleRepository.findByName(ERole.ROLE_PATIENT)
                                .orElseThrow(() -> new RuntimeException("error: role is not found."));
                        roles.add(patRole);

                        break;
                    case "secretaire":
                        Role secRole = roleRepository.findByName(ERole.ROLE_SECRETAIRE)
                                .orElseThrow(() -> new RuntimeException("error: role is not found."));
                        roles.add(secRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_PATIENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userService.sendRegistrationConfirmationEmail22(user , signUpRequest.getPassword());

        userRepository.save(user);
        //notificationService.createUserNotification(user);
        return ok("user registered successfully!  Un email a été envoyé au proprietaire du compte");
    }

    @GetMapping("/admin/activate/{id}")
    public ResponseEntity activateUser(@PathVariable(value = "id")int id){
        Map<Object, Object> model = new HashMap<>();
        try {
            userService.activateUser(id);
            model.put("message","user activate successfully !!!!!");
            return  new ResponseEntity<>(model, HttpStatus.OK);
        } catch (NotFoundException e) {
            model.put("message",e.getMessage());
            return  new ResponseEntity<>(model, HttpStatus.CREATED);
        }
    }

    @GetMapping("/getusers/me")
    private ResponseEntity currentUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            Map<Object, Object> model = new HashMap<>();
            if(userDetails!=null) {

                model.put("id",userDetails.getId());
                model.put("nomComplet",userDetails.getUsername());
                model.put("email", userDetails.getUsername());
                model.put("passwword",userDetails.getPassword());
                model.put("roles", userDetails.getAuthorities()
                        .stream()
                        .map(a -> ((GrantedAuthority) a).getAuthority())
                        .collect(toList())
                );
                return ok(model);
            }
            else {
                model.put("message","Not logged in !!!!!");
                return  new ResponseEntity(model,HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            Map<Object, Object> model = new HashMap<>();
            model.put("message",e.getMessage());
            return new ResponseEntity<>(model,HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/getusers/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id")int id){
        try {
            User country =userService.getUserbyID(id);
            //return country;
            return  new ResponseEntity<User>(country, HttpStatus.FOUND);
        }catch (Exception e) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/getusers")
    public ResponseEntity<List<User>> getUsers() {
        try {
            List<User> countries= userService.getAllUsers();
            return  new ResponseEntity< List<User>>(countries,HttpStatus.FOUND);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getusers/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam(value = "email") String email){
        try {
            User country =userService.getUserbyEmal(email);
            //return country;
            return  new ResponseEntity<User>(country, HttpStatus.FOUND);
        }catch (NoSuchElementException | NotFoundException e) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteuser/{id}")
    public ResponseEntity deleteUser(@PathVariable(value = "id") int id, Authentication authentication) {
        Map<Object, Object> model = new HashMap<>();
        User user = null;
        try {
            user = userService.getUserbyID(id);
            String email = user.getEmail();
            Set<Role> userRoles = user.getRoles();

            boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            boolean isDeletingPatient = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MEDECIN")) && userRoles.stream().anyMatch(r -> r.getName().equals("ROLE_PATIENT"));
            boolean isDeletingMedecin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) && userRoles.stream().anyMatch(r -> r.getName().equals("ROLE_MEDECIN"));

            if (isAdmin || isDeletingPatient || isDeletingMedecin) {
                userService.deleteUser(user);
                model.put("message","User deleted successfully!");
                return new ResponseEntity<>(model,HttpStatus.OK);
            } else {
                model.put("message","You are not authorized to perform this operation.");
                return new ResponseEntity<>(model,HttpStatus.UNAUTHORIZED);
            }
        } catch (NoSuchElementException e) {
            model.put("message",e.getMessage());
            return  new ResponseEntity<>(model,HttpStatus.NOT_FOUND);
        } catch (NotFoundException e) {
            model.put("message",e.getMessage());
            return new ResponseEntity<>(model,HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/send_mail/forgotPassword")
    public ResponseEntity forgotpassword (@RequestBody ForgottenPassword forgottenPassword ) {
        try {
            User user = userService.getUserbyEmal(forgottenPassword.getEmail());
            userService.sendResetPasswordEmail(user);
            Map<Object, Object> model = new HashMap<>();
            model.put("message","Email send to your account!!!!!!");
            return  new ResponseEntity(model,HttpStatus.OK);
        } catch (NotFoundException e) {
            return  new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/password/reset")
    public String forgotpasswordverifyToken (@RequestParam("token") String token , RedirectAttributes attributes, final Model model) {
        if(!StringUtils.hasText(token)) {
            attributes.addFlashAttribute("tokenError",
                    messageSource.getMessage("user.registration.verification.missing.token", null, LocaleContextHolder.getLocale())
            );
            return REDIRECT_LOGIN;
        }
        ResetPassword changePasswordDto = new ResetPassword();
        changePasswordDto.setToken(token);
        setResetPasswordForm(model , changePasswordDto);
        return  "redirect:/changePassword";
    }

    private void setResetPasswordForm(Model model, ResetPassword changePasswordDto) {
        model.addAttribute("forgotPassword",changePasswordDto);
    }


    @PutMapping("/password/reset")
    public ResponseEntity resetPassword (@RequestBody ResetPassword resetPassword) {
        Map<Object, Object> model = new HashMap<>();
        try {
            userService.resetPassword(resetPassword.getToken(),resetPassword);
            model.put("message","Password updated successfully!!!!!");
            return  new ResponseEntity(model,HttpStatus.OK);
        }catch (Exception e) {
            model.put("message",e.getMessage());
            return  new ResponseEntity(model,HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/change-password")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> changePassword(@Validated @RequestBody ChangePasswordDto changePasswordDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        try {
            userService.changePassword(userDetails.getEmail(),changePasswordDto);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully!");
    }

    @PostMapping("/restart/session")
    public ResponseEntity restartSession (@RequestBody LoginRequest loginRequest , @AuthenticationPrincipal UserDetailsImpl userDetails){
        Map<Object, Object> model = new HashMap<>();
        if(userDetails !=null) {
            if(encoder.matches(loginRequest.getPassword(),userDetails.getPassword())) {
                User authentication = new User(userDetails.getUsername(),userDetails.getLastName(),userDetails.getEmail(),userDetails.getPassword());
                List<String> roles = userDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(toList());
                return ResponseEntity.ok()
                        .body(new LoginResponse(
                                userDetails.getId(),userDetails.getUsername(),userDetails.getLastName(),userDetails.getPassword(),userDetails.getEmail() , roles
                        ));
            }
            else {
                model.put("message","Mot de passe incorrect !!!!!");
                return  new ResponseEntity(model,HttpStatus.BAD_REQUEST);
            }
        }
        else {
            try {
                if(loginRequest.getEmail()!=null) {
                    User user = userService.getUserbyEmal(loginRequest.getEmail());
                    if(encoder.matches(loginRequest.getPassword(),user.getPassword())) {
                        List<String> roles = user.getRoles().stream()
                                .map(role ->role.getName().toString())
                                .collect(toList());
                        return ResponseEntity.ok()
                                .body(new LoginResponse(user.getId(),user.getFirstName(),user.getLastName(),user.getEmail(), user.getPassword(), roles
                                ));
                    }
                    else {
                        model.put("message","Mot de passe incorrect !!!!!");
                        return  new ResponseEntity(model,HttpStatus.BAD_REQUEST);
                    }
                }
                else {
                    model.put("message","Votre sesson a été completement fermé veuillez vous reconnecté !!!!!");
                    return  new ResponseEntity(model,HttpStatus.BAD_REQUEST);
                }
            } catch (NotFoundException e) {
                model.put("message","Votre sesson a été completement fermé veuillez vous reconnecté !!!!!");
                return  new ResponseEntity(model,HttpStatus.BAD_REQUEST);
            }
        }
    }

}
