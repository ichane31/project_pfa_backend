package com.example.pfa_backend.controllers;

import com.example.pfa_backend.exception.NotFoundException;
import com.example.pfa_backend.models.*;
import com.example.pfa_backend.payload.request.*;
import com.example.pfa_backend.payload.response.LoginResponse;
import com.example.pfa_backend.repositories.*;
import com.example.pfa_backend.security.Services.UserDetailsImpl;
import com.example.pfa_backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    MedecinRepository medecinRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    SecretaireRepository secretaireRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    UserService userService;

    //@ApiOperation(value = "Get a resource by ID", response = Resource.class)
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Map<String, Object> model = new HashMap<>();

        try {
            User user = userService.getUserbyEmal(loginRequest.getEmail());

            if(user!=null) {
                if (user.isLoginDisabled()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Your account has been disabled due to too many failed login attempts");
                }
                if(!user.isAccountVerified()) {
                    model.put("message","Votre compte n'est pas activé.Veuillez contacter votre administrateur.!!!!!");
                    return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);
                }
                if(!encoder.matches(loginRequest.getPassword(),user.getPassword())) {
                    userService.incrementFailedLoginAttempts(user);
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

                model.put("id", userDetails.getId());
                model.put("nom", userDetails.getLastName());
                model.put("prenom", userDetails.getUsername());
                model.put("email", userDetails.getEmail());
                model.put("password", loginRequest.getPassword() );
                model.put("roles", roles);
                // Vérifier le rôle de l'utilisateur
                if (roles.contains("ROLE_MEDECIN")) {
                    // Récupérer le médecin associé à cet utilisateur
                    Medecin optionalMedecin = medecinRepository.findByEmail(userDetails.getEmail());
                    if (optionalMedecin !=null) {
                        
                        model.put("medecin", optionalMedecin);
                        // Ajouter d'autres informations spécifiques au médecin si nécessaire
                    }
                    else{
                        model.put("medecin", "Not trouve");
                    }
                } else if (roles.contains("ROLE_SECRETAIRE")) {
                    // Récupérer le secrétaire associé à cet utilisateur
                    Secretaire optionalSecretaire = secretaireRepository.findByEmail(userDetails.getEmail());
                    if (optionalSecretaire !=null) {
                        // Ajouter les informations spécifiques au secrétaire si nécessaire
                        model.put("secretaire", optionalSecretaire);

                    }
                }
                else if (roles.contains("ROLE_PATIENT")) {
                    // Récupérer le secrétaire associé à cet utilisateur
                    Patient patient = patientRepository.findByEmail(userDetails.getEmail());
                    if (patient !=null) {
                        // Ajouter les informations spécifiques au secrétaire si nécessaire
                        model.put("patient", patient);

                    }
                }
                else if (roles.contains("ROLE_ADMIN")) {                
                        model.put("admin", 1);
                }
                return ok(model);
                }

            model.put("message","User not found !!!!!");

            return new ResponseEntity<>(model,HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
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
            if(patientRepository.findByEmail(signUpRequest.getEmail())==null){
                Patient patient = new Patient();
                patient.setNom(signUpRequest.getLastName());
                patient.setPrenom(signUpRequest.getFirstName());
                patient.setEmail(signUpRequest.getEmail());
                patientRepository.save(patient);
            }
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
                        if(medecinRepository.findByEmail(signUpRequest.getEmail())==null) {
                            Medecin medecin = new Medecin();
                            medecin.setNom(signUpRequest.getLastName());
                            medecin.setPrenom(signUpRequest.getFirstName());
                            medecin.setEmail(signUpRequest.getEmail());
                            medecinRepository.save(medecin);
                        }

                        break;
                    case "patient":
                        Role patRole = roleRepository.findByName(ERole.ROLE_PATIENT)
                                .orElseThrow(() -> new RuntimeException("error: role is not found."));
                        roles.add(patRole);
                        if(patientRepository.findByEmail(signUpRequest.getEmail())==null){
                            Patient patient = new Patient();
                            patient.setNom(signUpRequest.getLastName());
                            patient.setPrenom(signUpRequest.getFirstName());
                            patient.setEmail(signUpRequest.getEmail());
                            patientRepository.save(patient);
                        }
                        break;
                    case "secretaire":
                        Role secRole = roleRepository.findByName(ERole.ROLE_SECRETAIRE)
                                .orElseThrow(() -> new RuntimeException("error: role is not found."));
                        roles.add(secRole);
                        if(secretaireRepository.findByEmail(signUpRequest.getEmail())==null) {
                            Secretaire secretaire = new Secretaire();
                            secretaire.setNom(signUpRequest.getLastName());
                            secretaire.setPrenom(signUpRequest.getFirstName());
                            secretaire.setEmail(signUpRequest.getEmail());
                            secretaireRepository.save(secretaire);
                        }

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_PATIENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                        if(patientRepository.findByEmail(signUpRequest.getEmail())==null){
                            Patient patient1 = new Patient();
                            patient1.setNom(signUpRequest.getLastName());
                            patient1.setPrenom(signUpRequest.getFirstName());
                            patient1.setEmail(signUpRequest.getEmail());
                            patientRepository.save(patient1);
                        }
                }
            });
        }
        user.setRoles(roles);
        user.setCreatedAt(LocalDateTime.now());
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


/* 
    @GetMapping("/getusers/me")
    private ResponseEntity currentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Map<String, Object> model = new HashMap<>();

        try {
            if(userDetails!=null) {
            model.put("id", userDetails.getId());
            model.put("nom", userDetails.getLastName());
            model.put("prenom", userDetails.getUsername());
            model.put("email", userDetails.getEmail());
            model.put("password", userDetails.getPassword());
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            model.put("roles", roles);

                return ok(model);
            }
            else {
                model.put("message","Not logged in !!!!!");
                return  new ResponseEntity(model,HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            model.put("message", e.getMessage());
            return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);
        }
    }
*/

@GetMapping("/getusers/me")
private ResponseEntity currentUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
    Map<String, Object> model = new HashMap<>();

    try {
        if(userDetails != null) {
            model.put("id", userDetails.getId());
            model.put("nom", userDetails.getLastName());
            model.put("prenom", userDetails.getUsername());
            model.put("email", userDetails.getEmail());
            model.put("password", userDetails.getPassword());
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            model.put("roles", roles);

            // Vérifier le rôle de l'utilisateur
            if (roles.contains("ROLE_MEDECIN")) {
                // Récupérer le médecin associé à cet utilisateur
                Medecin optionalMedecin = medecinRepository.findByEmail(userDetails.getEmail());
                if (optionalMedecin !=null) {
                    
                    model.put("medecin", optionalMedecin);
                    // Ajouter d'autres informations spécifiques au médecin si nécessaire
                }
                else{
                    model.put("medecin", "Not trouve");
                }
            } else if (roles.contains("ROLE_SECRETAIRE")) {
                // Récupérer le secrétaire associé à cet utilisateur
                Secretaire optionalSecretaire = secretaireRepository.findByEmail(userDetails.getEmail());
                if (optionalSecretaire !=null) {
                    // Ajouter les informations spécifiques au secrétaire si nécessaire
                    model.put("secretaire", optionalSecretaire);

                }
            }
            else if (roles.contains("ROLE_PATIENT")) {
                // Récupérer le secrétaire associé à cet utilisateur
                Patient patient = patientRepository.findByEmail(userDetails.getEmail());
                if (patient !=null) {
                    // Ajouter les informations spécifiques au secrétaire si nécessaire
                    model.put("patient", patient);

                }
            }
            else if (roles.contains("ROLE_ADMIN")) {                
                    model.put("admin", 1);
            }
            return ok(model);
        } else {
            model.put("message","Not logged in!");
            return new ResponseEntity(model,HttpStatus.BAD_REQUEST);
        }
    } catch (Exception e) {
        model.put("message", e.getMessage());
        return new ResponseEntity<>(model, HttpStatus.BAD_REQUEST);
    }
}


    @GetMapping("/getusers/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id")int id){
        try {
            User user =userService.getUserbyID(id);
            //return user;
            return  new ResponseEntity<User>(user, HttpStatus.OK);
        }catch (Exception e) {
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/admin/getusers")
    public ResponseEntity<List<User>> getUsers() {
        try {
            List<User> countries= userService.getAllUsers();
            return  new ResponseEntity<>(countries,HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getusers/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam(value = "email") String email){
        try {
            User user =userService.getUserbyEmal(email);
            //return user;
            return  new ResponseEntity<User>(user, HttpStatus.OK);
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
    public ResponseEntity<?> changePassword(@Validated @RequestBody ChangePasswordDto changePasswordDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        if(userDetails !=null) {
            try {
                userService.changePassword(userDetails.getEmail(),changePasswordDto);
            } catch (NotFoundException e) {
                return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
            }
    
            return ResponseEntity.status(HttpStatus.OK).body("password changed successfully!");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found!");
        }
        
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

    @DeleteMapping("/me/account")
    public ResponseEntity updateUser (@Valid @RequestBody String currentPassword ,@AuthenticationPrincipal UserDetailsImpl userDetails){
        Map<Object, Object> model = new HashMap<>();
        if(userDetails ==null) {
            model.put("message","Not logged in!!!!!");
            return  new ResponseEntity(model,HttpStatus.BAD_REQUEST);
        }else {
            if(!encoder.matches(currentPassword,userDetails.getPassword())){
                model.put("message","Mot de passe incorrect !!!!!");
                return  new ResponseEntity(model,HttpStatus.BAD_REQUEST);
            }
            else {
                try {
                    User user = userService.getUserbyEmal(userDetails.getEmail());
                    String email = user.getEmail();
                    userService.deleteUser(user);
                    model.put("message","Votre compte a été supprimer !!!!!");
                    return  new ResponseEntity(model,HttpStatus.OK);
                } catch (NotFoundException e) {
                    model.put("message",e.getMessage());
                    return  new ResponseEntity(model,HttpStatus.BAD_REQUEST);
                }
            }
        }
    }

}
