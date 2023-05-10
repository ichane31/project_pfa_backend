package com.example.pfa_backend.services;

import com.example.pfa_backend.email.context.RendezVousPatientEmailContext;
import com.example.pfa_backend.email.service.EmailServiceConf;
import com.example.pfa_backend.exception.InvalidException;
import com.example.pfa_backend.exception.NotFoundException;
import com.example.pfa_backend.models.*;
import com.example.pfa_backend.payload.request.RendezVousRequest;
import com.example.pfa_backend.repositories.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RendezVousService {

    @Autowired
    private RendezVousRepository rendezVousRepository;

    @Autowired
    private EmailServiceConf emailServiceConf;

    public RendezVous getById(Long id) {
        return rendezVousRepository.findById(id).orElse(null);
    }

    public List<RendezVous> getAll() {
        return rendezVousRepository.findAll();
    }

    public List<RendezVous> getByMedecinId(Long medecinId) {
        return rendezVousRepository.findByMedecinId(medecinId);
    }

    public List<RendezVous> getByPatientId(Long patientId) {
        return rendezVousRepository.findByPatientId(patientId);
    }

    public List<RendezVous> getByDate(String date) {
        return rendezVousRepository.findByDate(date);
    }

    public List<RendezVous> getByMedecinIdAndDate(Medecin medecin, String date) {
        return rendezVousRepository.findByDateAndMedecin( date ,medecin);
    }

    public void deleteById(Long id) {
        rendezVousRepository.deleteById(id);
    }

    public List<RendezVous> getRendezVousAujourdhui() {
        LocalDate aujourdhui = LocalDate.now();
        String auj = aujourdhui.toString();
        return rendezVousRepository.findByDate(auj);
    }

    public void sendRendezVousConfirmationEmail(RendezVous rendezVous ) {
        RendezVousPatientEmailContext emailContext = new RendezVousPatientEmailContext();
        emailContext.init(rendezVous);
        try {
            emailServiceConf.sendMail(emailContext);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public RendezVous saveRendezVous(RendezVousRequest rendezVousRequest, Medecin medecin, Patient patient) throws InvalidException {
        if(rendezVousRepository.findByMedecinAndDateAndHeureDebutAndHeureFin(medecin , rendezVousRequest.getDate(), rendezVousRequest.getHeureDebut(),rendezVousRequest.getHeureFin() ) !=null) {
            throw new InvalidException("Le medecin a déja un rendez programmée");
        }
        RendezVous rendezVous = new RendezVous();
        rendezVous.setDate(rendezVousRequest.getDate());
        rendezVous.setHeureDebut(rendezVousRequest.getHeureDebut());
        rendezVous.setHeureFin(rendezVousRequest.getHeureFin());
        rendezVous.setMotif(rendezVousRequest.getMotif());
        rendezVous.setMedecin(medecin);
        rendezVous.setPatient(patient);
        rendezVous.setStatusRDV(StatusRDV.PENDING);
        return rendezVousRepository.save(rendezVous);
    }

    public RendezVous updateRendezVous(Long id, RendezVousRequest rendezVousRequest) throws NotFoundException, InvalidException {
        Optional<RendezVous> optionalRendezVous = rendezVousRepository.findById(id);
        LocalDateTime now = LocalDateTime.now();
        if (optionalRendezVous.isPresent()) {
            RendezVous rendezVous = optionalRendezVous.get();
            if (rendezVous.getStatusRDV() == StatusRDV.DONE || rendezVous.getStatusRDV()==StatusRDV.CANCELED) {
                throw new InvalidException("La modification du rendez-vous n'est pas autorisée.");
            }
            rendezVous.setDate(rendezVousRequest.getDate());
            rendezVous.setHeureDebut(rendezVousRequest.getHeureDebut());
            rendezVous.setHeureFin(rendezVousRequest.getHeureFin());
            rendezVous.setMotif(rendezVousRequest.getMotif());
            //rendezVous.setStatusRDV(StatusRDV.CONFIRMED);
            sendRendezVousConfirmationEmail(rendezVous);
            return rendezVousRepository.save(rendezVous);
        } else {
            throw new NotFoundException("Rendez-vous non trouvé avec l'identifiant " + id);
        }
    }

    public List<RendezVous> findByMotifContainingIgnoreCase(String motif) {
        return rendezVousRepository.findByMotifContainingIgnoreCase(motif);
    }
    public RendezVous confirmedRendezVous(Long id) throws NotFoundException, InvalidException {
        Optional<RendezVous> optionalRendezVous = rendezVousRepository.findById(id);
        LocalDateTime now = LocalDateTime.now();
        if (optionalRendezVous.isPresent()) {
            RendezVous rendezVous = optionalRendezVous.get();
            if (rendezVous.getStatusRDV() == StatusRDV.DONE || rendezVous.getStatusRDV()==StatusRDV.CANCELED) {
                throw new InvalidException("La modification du rendez-vous n'est pas autorisée.");
            }
            rendezVous.setStatusRDV(StatusRDV.CONFIRMED);
            sendRendezVousConfirmationEmail(rendezVous);
            return rendezVousRepository.save(rendezVous);
        } else {
            throw new NotFoundException("Rendez-vous non trouvé avec l'identifiant " + id);
        }
    }

    public RendezVous canceledRendezVous(Long id) throws NotFoundException, InvalidException {
        Optional<RendezVous> optionalRendezVous = rendezVousRepository.findById(id);
        if (optionalRendezVous.isPresent()) {
            RendezVous rendezVous = optionalRendezVous.get();
            if (rendezVous.getStatusRDV() == StatusRDV.DONE || rendezVous.getStatusRDV()==StatusRDV.CANCELED) {
                throw new InvalidException("La modification du rendez-vous n'est pas autorisée.");
            }
            /* 
            if(rendezVous.getStatusRDV() == StatusRDV.CONFIRMED) {
                throw new InvalidException("Votre rendez a déja éte confirmed.Veuillez contacter le medecin.");
            }*/
            rendezVous.setStatusRDV(StatusRDV.CANCELED);
            //sendRendezVousConfirmationEmail(rendezVous);
            return rendezVousRepository.save(rendezVous);
        } else {
            throw new NotFoundException("Rendez-vous non trouvé avec l'identifiant " + id);
        }
    }

    public List<LocalTime>  getHeureDisponibles (Medecin medecin , String date) {
        // Récupérer tous les rendez-vous pour le médecin et la date spécifiés
        List<RendezVous> rendezVousList = rendezVousRepository.findByDateAndMedecin(date , medecin);

// Créer une liste d'heures disponibles pour la journée
        List<LocalTime> heuresDisponibles = new ArrayList<>();
        heuresDisponibles.add(LocalTime.of(8, 0)); // Heure d'ouverture du cabinet
        heuresDisponibles.add(LocalTime.of(8, 30));
        heuresDisponibles.add(LocalTime.of(9, 0));
        heuresDisponibles.add(LocalTime.of(9, 30));
        heuresDisponibles.add(LocalTime.of(10, 0));
        heuresDisponibles.add(LocalTime.of(10, 30));
        heuresDisponibles.add(LocalTime.of(11, 0));
        heuresDisponibles.add(LocalTime.of(11, 30));
        heuresDisponibles.add(LocalTime.of(12, 0));
        heuresDisponibles.add(LocalTime.of(12, 30));
        heuresDisponibles.add(LocalTime.of(13, 0));
        heuresDisponibles.add(LocalTime.of(13, 30));
        heuresDisponibles.add(LocalTime.of(14, 0));
        heuresDisponibles.add(LocalTime.of(14, 30));
        heuresDisponibles.add(LocalTime.of(15, 0));
        heuresDisponibles.add(LocalTime.of(15, 30));
        heuresDisponibles.add(LocalTime.of(16, 0));
        heuresDisponibles.add(LocalTime.of(16, 30));
        heuresDisponibles.add(LocalTime.of(17, 0)); // Heure de fermeture du cabinet

        // Parcourir la liste des rendez-vous pour déterminer les heures disponibles
        for (RendezVous rdv : rendezVousList) {
            heuresDisponibles.removeIf(h -> h.isAfter(LocalTime.parse(rdv.getHeureDebut())) && h.isBefore(LocalTime.parse(rdv.getHeureFin())));
        }

// Afficher les heures disponibles
       return  heuresDisponibles;

    }
}
