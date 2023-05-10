package com.example.pfa_backend.email.context;

import com.example.pfa_backend.models.User;
import org.springframework.web.util.UriComponentsBuilder;

public class AccountVerificationEmailContext extends AbstractEmailContext{

    private String token;

    @Override
    public <T> void init(T context ,String password){
        //we can do any common configuration setup here
        // like setting up some base URL and context
        User user = (User) context; // we pass the customer informati
        put("Nom_Prenom", user.getFirstName() + " " +user.getLastName());
        put("Email", user.getEmail());
        put("Password", password);
        setTemplateLocation("account_email");
        setSubject("Your Accord Informations");
        setFrom("projectensaj2023@gmail.com");
        setTo(user.getEmail());
    }

    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }

    public void buildVerificationUrl(final String baseURL, final String token){
        final String url= UriComponentsBuilder.fromHttpUrl(baseURL)
                .path("/signup/verify").queryParam("token", token).toUriString();
        put("verificationURL", url);
    }
}
