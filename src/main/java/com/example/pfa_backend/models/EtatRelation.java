package com.example.pfa_backend.models;

public enum EtatRelation {
    ACTIVE("active"),
    INACTIVE("inactive");

    private final String value;

    EtatRelation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static EtatRelation fromValue(String value) {
        for (EtatRelation etatRelation : values()) {
            if (etatRelation.getValue().equalsIgnoreCase(value)) {
                return etatRelation;
            }
        }
        throw new IllegalArgumentException("Invalid EtatRelation value: " + value);
    }
}
