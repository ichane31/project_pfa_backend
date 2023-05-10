package com.example.pfa_backend.payload.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ForgottenPassword {
    @NotBlank(message = "Email should not be null !!!")
    @Size(max = 50)
    @Email(message = "Veuillez saisir une address email valide")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
