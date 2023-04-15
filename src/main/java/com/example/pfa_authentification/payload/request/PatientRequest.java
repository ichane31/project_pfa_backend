package com.example.pfa_authentification.payload.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.time.LocalDate;

@Data
public class PatientRequest {

    private String nom;
    private String prenom;
    private LocalDate date_naissance;
    private int age;
    private String email;
    private String sexe;
    private String adresse;
    private String tel;
    private String goup_sanguin;
    private MultipartFile image;

}
