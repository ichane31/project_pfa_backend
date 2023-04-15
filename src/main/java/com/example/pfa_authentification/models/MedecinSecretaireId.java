package com.example.pfa_authentification.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class MedecinSecretaireId implements Serializable {

    @Column(name = "medecin_id")
    private Long medecinId;

    @Column(name = "secretaire_id")
    private Long secretaireId;


    public MedecinSecretaireId(Long medecinId, Long secretaireId) {
        this.medecinId = medecinId;
        this.secretaireId = secretaireId;
    }

    public MedecinSecretaireId() {

    }

    // getters and setters
    // equals and hashcode methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedecinSecretaireId)) return false;
        MedecinSecretaireId that = (MedecinSecretaireId) o;
        return Objects.equals(getMedecinId(), that.getMedecinId()) &&
                Objects.equals(getSecretaireId(), that.getSecretaireId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMedecinId(), getSecretaireId());
    }

    // toString method
}
