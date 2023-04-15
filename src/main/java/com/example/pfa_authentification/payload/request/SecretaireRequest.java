package com.example.pfa_authentification.payload.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.Date;

@Data
public class SecretaireRequest {
    private String nom;
    private String prenom;
    private Date date_naissance;
    private int age;
    private String email;
    private String sexe;
    private String adresse;
    private String tel;
    private  String cin;
    private LocalDate dateEmboche;
    private MultipartFile image;
}
