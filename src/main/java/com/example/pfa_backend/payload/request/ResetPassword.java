package com.example.pfa_backend.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetPassword {

    private String token;

    @NotBlank
    private String newPassword;

    private String repeatPassword;

}
