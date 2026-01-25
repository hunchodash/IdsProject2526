package it.hackhub.domain;

import java.time.LocalDateTime;

public class Sottomissione {
    private Long id;
    private Long hackathonId;
    private Long teamId;
    private String contenuto; // URL della repository o descrizione
    private LocalDateTime dataSottomissione;

    public Sottomissione(Long id, Long hackathonId, Long teamId, String contenuto) {
        this.id = id;
        this.hackathonId = hackathonId;
        this.teamId = teamId;
        this.contenuto = contenuto;
        this.dataSottomissione = LocalDateTime.now();
    }

    public Long getHackathonId() { return hackathonId; }
    public Long getTeamId() { return teamId; }
    public String getContenuto() { return contenuto; }
    public LocalDateTime getDataSottomissione() { return dataSottomissione; }
}