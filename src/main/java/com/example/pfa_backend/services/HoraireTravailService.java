package com.example.pfa_backend.services;

import com.example.pfa_backend.exception.NotFoundException;
import com.example.pfa_backend.models.HoraireTravail;
import com.example.pfa_backend.models.Medecin;
import com.example.pfa_backend.repositories.HoraireTravailRepository;
import com.example.pfa_backend.repositories.MedecinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Service
public class HoraireTravailService {

    @Autowired
    private HoraireTravailRepository horaireTravailRepository;

    @Autowired
    private MedecinRepository medecinRepository;

    public List<HoraireTravail> getAllHoraires() {
        return horaireTravailRepository.findAll();
    }

    public HoraireTravail getHoraireById(Long id) throws NotFoundException {
        return horaireTravailRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Horaire de travail id " + id));
    }

    public HoraireTravail addHoraire(HoraireTravail horaireTravail) {
        return horaireTravailRepository.save(horaireTravail);
    }

    public void ajouterHoraireTravail(Long medecinId, HoraireTravail horaireTravail) throws NotFoundException {
        Medecin medecin = medecinRepository.findById(medecinId)
                .orElseThrow(() -> new NotFoundException("Aucun médecin avec l'id " + medecinId));

        // associer le nouvel horaire de travail avec le médecin
        horaireTravail.setMedecin(medecin);

        // enregistrer le nouvel horaire de travail
        horaireTravailRepository.save(horaireTravail);
    }
    public HoraireTravail updateHoraire(Long id, HoraireTravail horaireTravailDetails) throws NotFoundException {
        HoraireTravail horaireTravail = horaireTravailRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Horaire de travail id "+ id));
        horaireTravail.setJourSemaine(horaireTravailDetails.getJourSemaine());
        horaireTravail.setHeureDebut(horaireTravailDetails.getHeureDebut());
        horaireTravail.setHeureFin(horaireTravailDetails.getHeureFin());
        horaireTravail.setMedecin(horaireTravailDetails.getMedecin());
        return horaireTravailRepository.save(horaireTravail);
    }

    public boolean isHoraireAvailable(Long medecinId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        List<HoraireTravail> horaires = getHorairesByMedecinAndDay(medecinId, dayOfWeek);
        for (HoraireTravail horaire : horaires) {
            LocalTime horaireDebut = LocalTime.parse(horaire.getHeureDebut());
            LocalTime horaireFin = LocalTime.parse(horaire.getHeureFin());
            if (startTime.isBefore(horaireDebut) || endTime.isAfter(horaireFin)) {
                // The requested timeslot is outside the doctor's working hours
                continue;
            }
            if (horaireDebut.isBefore(startTime) && horaireFin.isAfter(endTime)) {
                // The requested timeslot is within an existing appointment
                return false;
            }
        }
        return true;
    }

    public List<HoraireTravail> getHorairesByMedecinAndDay(Long medecinId, DayOfWeek jourSemaine) {
        // obtenir la liste des horaires de travail pour le médecin et le jour donné
        return horaireTravailRepository.findByMedecin_IdAndJourSemaine(medecinId, jourSemaine);
    }

    public void deleteHoraire(Long id) throws NotFoundException {
        HoraireTravail horaireTravail = horaireTravailRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Horaire de travail id "+ id));
        horaireTravailRepository.delete(horaireTravail);
    }

}
