package com.example.pfa_authentification.services;

import com.example.pfa_authentification.email.context.AccountVerificationEmailContext;
import com.example.pfa_authentification.email.context.RendezVousPatientEmailContext;
import com.example.pfa_authentification.email.service.EmailServiceConf;
import com.example.pfa_authentification.exception.InvalidException;
import com.example.pfa_authentification.exception.NotFoundException;
import com.example.pfa_authentification.models.*;
import com.example.pfa_authentification.payload.request.RendezVousRequest;
import com.example.pfa_authentification.repositories.RendezVousRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public List<RendezVous> getByDate(LocalDate date) {
        return rendezVousRepository.findByDate(date);
    }

    public List<RendezVous> getByMedecinIdAndDate(Medecin medecin, LocalDate date) {
        return rendezVousRepository.findByDateAndMedecin( date ,medecin);
    }

    public void deleteById(Long id) {
        rendezVousRepository.deleteById(id);
    }

    public List<RendezVous> getRendezVousAujourdhui() {
        LocalDate aujourdhui = LocalDate.now();
        return rendezVousRepository.findByDate(aujourdhui);
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
    public RendezVous saveRendezVous(RendezVousRequest rendezVousRequest, Medecin medecin, Patient patient) {
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
            sendRendezVousConfirmationEmail(rendezVous);
            return rendezVousRepository.save(rendezVous);
        } else {
            throw new NotFoundException("Rendez-vous non trouvé avec l'identifiant " + id);
        }
    }

    public List<RendezVous> findByMotifContainingIgnoreCase(String motif) {
        return rendezVousRepository.findByMotifContainingIgnoreCase(motif);
    }


}
