package com.example.pfa_backend.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TraitementRequest {
    @NotBlank
    private String name;

    private String description;

}
