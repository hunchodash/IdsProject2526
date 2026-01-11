package it.hackhub.domain;

public class Team {
    private Long id;
    private int numeroMembri;

    public Team(Long id, int numeroMembri) {
        this.id = id;
        this.numeroMembri = numeroMembri;
    }

    public Long getId() {
        return id;
    }

    public int getNumeroMembri() {
        return numeroMembri;
    }
}
