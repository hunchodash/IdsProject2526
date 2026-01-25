package it.hackhub.domain;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private Long id;
    private String nomeTeam;

    // ORA È CORRETTO: Lista di Utenti
    private List<Utente> membri;

    public Team(Long id, String nomeTeam) {
        this.id = id;
        this.nomeTeam = nomeTeam;
        this.membri = new ArrayList<>();
    }

    public void aggiungiMembro(Utente utente) {
        this.membri.add(utente);
    }

    public List<Utente> getMembri() {
        return membri;
    }

    public int getNumeroMembri() {
        return membri.size();
    }

    public Long getId() { return id; }
    public String getNomeTeam() { return nomeTeam; }
}