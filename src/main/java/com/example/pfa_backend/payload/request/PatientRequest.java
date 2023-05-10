package com.example.pfa_backend.payload.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;

@Data
public class PatientRequest {

    private String nom;
    private String prenom;
    private String date_naissance;
    private int age;
    private String email;
    private String sexe;
    private String adresse;
    private String tel;
    private String goup_sanguin;
    private String password;
    private MultipartFile image;

}
