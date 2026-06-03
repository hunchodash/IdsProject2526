package it.hackhub.domain;

import java.time.LocalDateTime;

public class Sottomissione {
    private Long id;
    private String contenuto;
    private LocalDateTime dataSottomissione;
    private Long hackathonId;
    private Long teamId;

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

    public Valutazione aggiornaValutazione(int punteggio, String commento) {
        return new Valutazione(hackathonId, teamId, punteggio, commento);
    }

    public Long getId() { return id; }
    public String getContenuto() { return contenuto; }
    public LocalDateTime getDataSottomissione() { return dataSottomissione; }
    public Long getHackathonId() { return hackathonId; }
    public Long getTeamId() { return teamId; }
}
