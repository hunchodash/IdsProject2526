package it.hackhub.domain;

import it.hackhub.domain.state.StatoHackathon;

public class Hackathon {
    private Long id;
    private int maxTeamSize;
    private StatoHackathon stato;

    public Hackathon(Long id, int maxTeamSize, StatoHackathon stato) {
        this.id = id;
        this.maxTeamSize = maxTeamSize;
        this.stato = stato;
    }

    public Long getId() { return id; }
    public int getMaxTeamSize() { return maxTeamSize; }
    public StatoHackathon getStato() { return stato; }
    public void setStato(StatoHackathon stato) { this.stato = stato; }

    // Logica di business: l'Hackathon sa se un team va bene per lui
    public boolean isTeamSizeValida(int size) {
        return size > 0 && size <= this.maxTeamSize;
    }
}