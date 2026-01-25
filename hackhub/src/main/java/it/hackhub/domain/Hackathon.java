package it.hackhub.domain;

import it.hackhub.domain.staff.Organizzatore;
import it.hackhub.domain.staff.Giudice;
import it.hackhub.domain.staff.Mentore;
import it.hackhub.domain.state.StatoHackathon;
import java.util.ArrayList;
import java.util.List;

public class Hackathon {
    private Long id;
    private String nome; // Aggiunto per completezza
    private int maxTeamSize;
    private StatoHackathon stato;

    // Lo staff assegnato come da requisiti
    private Organizzatore organizzatore;
    private Giudice giudice;
    private List<Mentore> mentori;

    public Hackathon(Long id, String nome, int maxTeamSize, StatoHackathon stato, Organizzatore organizzatore, Giudice giudice) {
        this.id = id;
        this.nome = nome;
        this.maxTeamSize = maxTeamSize;
        this.stato = stato;
        this.organizzatore = organizzatore;
        this.giudice = giudice;
        this.mentori = new ArrayList<>();
    }

    // Metodi per gestire i mentori (Requisito: L'organizzatore può aggiungerne altri dopo)
    public void aggiungiMentore(Mentore mentore) {
        if (!mentori.contains(mentore)) {
            this.mentori.add(mentore);
        }
    }

    // Getters
    public Long getId() { return id; }
    public int getMaxTeamSize() { return maxTeamSize; }
    public StatoHackathon getStato() { return stato; }
    public Organizzatore getOrganizzatore() { return organizzatore; }
    public Giudice getGiudice() { return giudice; }
    public List<Mentore> getMentori() { return mentori; }

    // Setters e Logica
    public void setStato(StatoHackathon stato) { this.stato = stato; }

    public boolean isTeamSizeValida(int size) {
        return size > 0 && size <= this.maxTeamSize;
    }
}