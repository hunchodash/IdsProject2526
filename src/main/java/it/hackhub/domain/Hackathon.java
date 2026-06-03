package it.hackhub.domain;

import it.hackhub.domain.staff.Mentore;
import it.hackhub.domain.staff.Organizzatore;
import it.hackhub.domain.staff.Giudice;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hackathon {
    private Long id;
    private String nome;
    private String regolamento;
    private LocalDateTime scadenzaIscrizioni;
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;
    private int maxTeamSize;
    private Organizzatore organizzatore;
    private Giudice giudice;
    private final List<Mentore> mentori = new ArrayList<>();
    private final List<Iscrizione> iscrizioni = new ArrayList<>();
    private final List<Sottomissione> sottomissioni = new ArrayList<>();

    public Hackathon(Long id, String nome, String regolamento,
                     LocalDateTime scadenzaIscrizioni, LocalDateTime dataInizio,
                     LocalDateTime dataFine, int maxTeamSize,
                     Organizzatore organizzatore, Giudice giudice) {
        this.id = id;
        this.nome = nome;
        this.regolamento = regolamento;
        this.scadenzaIscrizioni = scadenzaIscrizioni;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.maxTeamSize = maxTeamSize;
        this.organizzatore = organizzatore;
        this.giudice = giudice;
    }

    public void aggiungiMentore(Mentore mentore) {
        if (!mentori.contains(mentore)) {
            mentori.add(mentore);
        }
    }

    public void iscriviTeam(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("Team obbligatorio");
        }
        if (!isIscrizioniAperte()) {
            throw new IllegalStateException("Iscrizioni chiuse");
        }
        if (team.getDimensioneTeam() > maxTeamSize) {
            throw new IllegalArgumentException("Il team supera il numero massimo di membri");
        }
        if (verificaGiaIscritto(team.getId())) {
            throw new IllegalStateException("Team già iscritto");
        }
        iscrizioni.add(new Iscrizione(id, team.getId()));
    }

    public void associaSottomissione(Sottomissione sottomissione) {
        if (sottomissione == null) {
            throw new IllegalArgumentException("Sottomissione obbligatoria");
        }
        sottomissioni.add(sottomissione);
    }

    public String recuperaDatiEvento() {
        return nome + " - scadenza iscrizioni: " + scadenzaIscrizioni + ", max team: " + maxTeamSize;
    }

    public boolean verificaGiaIscritto(Long teamId) {
        return iscrizioni.stream().anyMatch(i -> i.getTeamId().equals(teamId));
    }

    public boolean isIscrizioniAperte() {
        return LocalDateTime.now().isBefore(scadenzaIscrizioni);
    }

    public boolean isInCorso() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(dataInizio) && now.isBefore(dataFine);
    }

    public void salvaNelSistema() {
        // Metodo presente nel diagramma: il salvataggio reale è delegato al repository in memoria.
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getRegolamento() { return regolamento; }
    public LocalDateTime getScadenzaIscrizioni() { return scadenzaIscrizioni; }
    public LocalDateTime getDataInizio() { return dataInizio; }
    public LocalDateTime getDataFine() { return dataFine; }
    public int getMaxTeamSize() { return maxTeamSize; }
    public Organizzatore getOrganizzatore() { return organizzatore; }
    public Giudice getGiudice() { return giudice; }
    public List<Mentore> getMentori() { return Collections.unmodifiableList(mentori); }
    public List<Iscrizione> getIscrizioni() { return Collections.unmodifiableList(iscrizioni); }
    public List<Sottomissione> getSottomissioni() { return Collections.unmodifiableList(sottomissioni); }
}
