package it.hackhub.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Team {
    private Long id;
    private String nomeTeam;
    private final List<Utente> membri = new ArrayList<>();

    public Team(Long id, String nomeTeam) {
        this.id = id;
        this.nomeTeam = nomeTeam;
    }

    public void aggiungiMembro(Utente utente) {
        if (!membri.contains(utente)) {
            membri.add(utente);
        }
    }

    public void registraNuovaSottomissione(String contenuto) {
        if (contenuto == null || contenuto.isBlank()) {
            throw new IllegalArgumentException("Il contenuto della sottomissione è obbligatorio");
        }
    }

    public int getDimensioneTeam() { return membri.size(); }
    public Long getId() { return id; }
    public String getNomeTeam() { return nomeTeam; }
    public List<Utente> getMembri() { return Collections.unmodifiableList(membri); }
}
