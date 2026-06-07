package it.hackhub.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sottomissione {
    private Long id;
    private String contenuto;
    private LocalDateTime dataSottomissione;
    private Long hackathonId;
    private Long teamId;
    private final List<Valutazione> valutazioni = new ArrayList<>();

    public Sottomissione(Long id, Long hackathonId, Long teamId, String contenuto) {
        if (contenuto == null || contenuto.isBlank()) {
            throw new IllegalArgumentException("Il contenuto della sottomissione è obbligatorio");
        }
        this.id = id;
        this.hackathonId = hackathonId;
        this.teamId = teamId;
        this.contenuto = contenuto;
        this.dataSottomissione = LocalDateTime.now();
    }

    public String getDettagli() {
        return "Team " + teamId + " - " + contenuto + " (" + dataSottomissione + ")";
    }

    public void aggiornaContenuto(String nuovoContenuto) {
        if (nuovoContenuto == null || nuovoContenuto.isBlank()) {
            throw new IllegalArgumentException("Il contenuto della sottomissione è obbligatorio");
        }
        this.contenuto = nuovoContenuto;
        this.dataSottomissione = LocalDateTime.now();
    }

    public Valutazione aggiornaValutazione(int punteggio, String commento) {
        return aggiornaValutazione(null, punteggio, commento);
    }

    public Valutazione aggiornaValutazione(Long giudiceId, int punteggio, String commento) {
        Valutazione valutazione = new Valutazione(System.currentTimeMillis(), hackathonId, teamId, id, giudiceId, punteggio, commento);
        valutazioni.add(valutazione);
        return valutazione;
    }

    public boolean risultaValutata() {
        return !valutazioni.isEmpty();
    }

    public double getPunteggioMedio() {
        return valutazioni.stream().mapToInt(Valutazione::getPunteggio).average().orElse(0.0);
    }

    public Long getId() { return id; }
    public String getContenuto() { return contenuto; }
    public LocalDateTime getDataSottomissione() { return dataSottomissione; }
    public Long getHackathonId() { return hackathonId; }
    public Long getTeamId() { return teamId; }
    public List<Valutazione> getValutazioni() { return Collections.unmodifiableList(valutazioni); }
}
