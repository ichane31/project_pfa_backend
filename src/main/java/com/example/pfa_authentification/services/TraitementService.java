package com.example.pfa_authentification.services;

import com.example.pfa_authentification.exception.InvalidException;
import com.example.pfa_authentification.exception.NotFoundException;
import com.example.pfa_authentification.models.Medecin;
import com.example.pfa_authentification.models.Traitement;
import com.example.pfa_authentification.payload.request.TraitementRequest;
import com.example.pfa_authentification.repositories.MedecinRepository;
import com.example.pfa_authentification.repositories.TraitementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TraitementService {

    @Autowired
    private TraitementRepository traitementRepository;

    @Autowired
    private MedecinRepository medecinRepository;

    public Traitement getById(Long id) {
        return traitementRepository.findById(id).orElse(null);
    }

    public List<Traitement> getAll() {
        return traitementRepository.findAll();
    }


    public Traitement create(Long medecinId,TraitementRequest traitementRequest) throws NotFoundException, InvalidException {
        // Vérifier si le médecin existe
        Medecin medecin = medecinRepository.findById(medecinId)
                .orElseThrow(() -> new NotFoundException("Aucun médecin avec l'id " + medecinId));

        // Vérifier si le médecin a déjà un traitement avec le même nom
        boolean traitementExists = medecin.getTraitements().stream()
                .anyMatch(traitement -> traitement.getName().equals(traitementRequest.getName()));

        if (traitementExists) {
            throw new InvalidException("Le médecin a déjà un traitement avec le nom " + traitementRequest.getName());
        }
        Traitement traitement = new Traitement();
        traitement.setName(traitementRequest.getName());
        traitement.setDescription(traitementRequest.getDescription());
        traitement.setMedecin(medecin);
        return traitementRepository.save(traitement);
    }

    public void deleteById(Long id) {
        traitementRepository.deleteById(id);
    }

    public Traitement update(Long id, TraitementRequest traitementRequest) throws NotFoundException {
        Traitement traitement = traitementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Traitement Not Found with id : " + id));

        traitement.setName(traitementRequest.getName());
        traitement.setDescription(traitementRequest.getDescription());

        // Update patient treatments
        return traitementRepository.save(traitement);
    }


    public  Traitement getByName(String name) {
        return  traitementRepository.findTraitementByName(name);
    }

    public List<Traitement> getByDescriptionContaining (String des) {
        return  traitementRepository.findTraitementByDescriptionContaining(des);
    }
}
