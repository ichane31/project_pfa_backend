package com.example.pfa_authentification.email.context;

import com.example.pfa_authentification.models.User;
import org.springframework.web.util.UriComponentsBuilder;

public class ForgotPasswordEmailContext extends AbstractEmailContext {

    private String token;

    @Override
    public <T> void init(T context){
        //we can do any common configuration setup here
        // like setting up some base URL and context
        User user = (User) context; // we pass the customer informati
        put("Nom_Prenom", user.getFirstName() + " " +user.getLastName());
        setTemplateLocation("forgot-password");
        setSubject("Forgotten Password");
        setFrom("projectensaj2023@gmail.com");
        setTo(user.getEmail());
    }

    @Override
    public <T> void init(T context, String password) {

    }

    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }

    public void buildVerificationUrl(final String baseURL, final String token){
        final String url= UriComponentsBuilder.fromHttpUrl(baseURL)
                .path("/resetPassword").queryParam("token", token).toUriString();
        put("verificationURL", url);
    }
}