package com.example.pfa_backend.payload.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class MedecinRequest {
    private String nom;
    private String prenom;
    private String date_naissance;
    private int age;
    private String email;
    private String sexe;
    private String adresse;
    private String tel;
    private String specialite;
    private  String cin;
    private String dateEmboche;
    private MultipartFile image;


}
