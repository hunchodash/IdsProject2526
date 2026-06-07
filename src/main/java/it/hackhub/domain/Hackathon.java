package it.hackhub.domain;

import it.hackhub.domain.staff.Giudice;
import it.hackhub.domain.staff.MembroStaff;
import it.hackhub.domain.staff.Mentore;
import it.hackhub.domain.staff.Organizzatore;

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
    private String luogo;
    private double premioInDenaro;
    private int maxTeamSize;
    private Organizzatore organizzatore;
    private HackathonStatus stato;
    private Team teamVincitore;

    private final List<Giudice> giudici = new ArrayList<>();
    private final List<Mentore> mentori = new ArrayList<>();
    private final List<Iscrizione> iscrizioni = new ArrayList<>();
    private final List<Sottomissione> sottomissioni = new ArrayList<>();

    public Hackathon(Long id, String nome, String regolamento,
                     LocalDateTime scadenzaIscrizioni, LocalDateTime dataInizio,
                     LocalDateTime dataFine, int maxTeamSize,
                     Organizzatore organizzatore, Giudice giudice) {
        this(id, nome, regolamento, scadenzaIscrizioni, dataInizio, dataFine, "Da definire", 0.0, maxTeamSize, organizzatore);
        if (giudice != null) {
            aggiungiGiudice(giudice);
        }
    }

    public Hackathon(Long id, String nome, String regolamento,
                     LocalDateTime scadenzaIscrizioni, LocalDateTime dataInizio,
                     LocalDateTime dataFine, int maxTeamSize,
                     Organizzatore organizzatore) {
        this(id, nome, regolamento, scadenzaIscrizioni, dataInizio, dataFine, "Da definire", 0.0, maxTeamSize, organizzatore);
    }

    public Hackathon(Long id, String nome, String regolamento,
                     LocalDateTime scadenzaIscrizioni, LocalDateTime dataInizio,
                     LocalDateTime dataFine, String luogo, double premioInDenaro, int maxTeamSize,
                     Organizzatore organizzatore) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome hackathon obbligatorio");
        }
        if (scadenzaIscrizioni == null || dataInizio == null || dataFine == null) {
            throw new IllegalArgumentException("Date hackathon obbligatorie");
        }
        if (scadenzaIscrizioni.isAfter(dataInizio)) {
            throw new IllegalArgumentException("La scadenza iscrizioni deve precedere o coincidere con la data di inizio");
        }
        if (!dataFine.isAfter(dataInizio)) {
            throw new IllegalArgumentException("La data di fine deve essere successiva alla data di inizio");
        }
        if (maxTeamSize <= 0) {
            throw new IllegalArgumentException("Numero massimo membri non valido");
        }
        if (luogo == null || luogo.isBlank()) {
            throw new IllegalArgumentException("Luogo obbligatorio");
        }
        if (premioInDenaro < 0) {
            throw new IllegalArgumentException("Premio in denaro non valido");
        }
        this.id = id;
        this.nome = nome;
        this.regolamento = regolamento;
        this.scadenzaIscrizioni = scadenzaIscrizioni;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.luogo = luogo;
        this.premioInDenaro = premioInDenaro;
        this.maxTeamSize = maxTeamSize;
        this.organizzatore = organizzatore;
        this.stato = calcolaStatoCorrente();
    }

    public void aggiungiGiudice(Giudice giudice) {
        if (giudice == null) {
            throw new IllegalArgumentException("Giudice obbligatorio");
        }
        if (!giudici.contains(giudice)) {
            giudici.add(giudice);
        }
    }

    public void aggiungiMentore(Mentore mentore) {
        if (mentore == null) {
            throw new IllegalArgumentException("Mentore obbligatorio");
        }
        if (!mentori.contains(mentore)) {
            mentori.add(mentore);
        }
    }

    public void aggiungiStaff(MembroStaff membroStaff, String ruolo) {
        if (membroStaff == null || ruolo == null || ruolo.isBlank()) {
            throw new IllegalArgumentException("Utente e ruolo sono obbligatori");
        }
        if ("GIUDICE".equalsIgnoreCase(ruolo) && membroStaff instanceof Giudice) {
            aggiungiGiudice((Giudice) membroStaff);
            return;
        }
        if ("MENTORE".equalsIgnoreCase(ruolo) && membroStaff instanceof Mentore) {
            aggiungiMentore((Mentore) membroStaff);
            return;
        }
        throw new IllegalArgumentException("Ruolo non coerente con il tipo di utente");
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
        if (!sottomissioni.contains(sottomissione)) {
            sottomissioni.add(sottomissione);
        }
    }

    public void registraIscrizionePreesistente(Team team) {
        if (team == null) {
            throw new IllegalArgumentException("Team obbligatorio");
        }
        if (team.getDimensioneTeam() > maxTeamSize) {
            throw new IllegalArgumentException("Il team supera il numero massimo di membri");
        }
        if (!verificaGiaIscritto(team.getId())) {
            iscrizioni.add(new Iscrizione(id, team.getId()));
        }
    }

    public void setVincitore(Team teamVincitore) {
        if (teamVincitore == null) {
            throw new IllegalArgumentException("Team vincitore obbligatorio");
        }
        if (!verificaGiaIscritto(teamVincitore.getId())) {
            throw new IllegalArgumentException("Il team vincitore deve essere iscritto all'hackathon");
        }
        this.teamVincitore = teamVincitore;
    }

    public void setStato(HackathonStatus stato) {
        if (stato == null) {
            throw new IllegalArgumentException("Stato obbligatorio");
        }
        this.stato = stato;
    }

    public String recuperaDatiEvento() {
        return nome + " - scadenza iscrizioni: " + scadenzaIscrizioni + ", max team: " + maxTeamSize;
    }

    public boolean verificaGiaIscritto(Long teamId) {
        return iscrizioni.stream().anyMatch(i -> i.getTeamId().equals(teamId));
    }

    public boolean isIscrizioniAperte() {
        aggiornaStatoSeNonConcluso();
        return stato == HackathonStatus.ISCRIZIONI_APERTE;
    }

    public boolean isInCorso() {
        aggiornaStatoSeNonConcluso();
        return stato == HackathonStatus.IN_CORSO;
    }

    public HackathonStatus calcolaStatoCorrente() {
        LocalDateTime now = LocalDateTime.now();
        if (teamVincitore != null || stato == HackathonStatus.CONCLUSO) {
            return HackathonStatus.CONCLUSO;
        }
        if (now.isBefore(scadenzaIscrizioni)) {
            return HackathonStatus.ISCRIZIONI_APERTE;
        }
        if (now.isBefore(dataFine)) {
            return HackathonStatus.IN_CORSO;
        }
        return HackathonStatus.VALUTAZIONE;
    }

    public void aggiornaStatoCorrente() {
        this.stato = calcolaStatoCorrente();
    }

    private void aggiornaStatoSeNonConcluso() {
        if (stato != HackathonStatus.CONCLUSO) {
            aggiornaStatoCorrente();
        }
    }

    public void salvaNelSistema() {
        // Metodo presente nei sequence diagram: il salvataggio reale è delegato al repository in memoria.
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getRegolamento() { return regolamento; }
    public LocalDateTime getScadenzaIscrizioni() { return scadenzaIscrizioni; }
    public LocalDateTime getDataInizio() { return dataInizio; }
    public LocalDateTime getDataFine() { return dataFine; }
    public String getLuogo() { return luogo; }
    public double getPremioInDenaro() { return premioInDenaro; }
    public int getMaxTeamSize() { return maxTeamSize; }
    public Organizzatore getOrganizzatore() { return organizzatore; }
    public Giudice getGiudice() { return giudici.isEmpty() ? null : giudici.get(0); }
    public List<Giudice> getGiudici() { return Collections.unmodifiableList(giudici); }
    public List<Mentore> getMentori() { return Collections.unmodifiableList(mentori); }
    public List<Iscrizione> getIscrizioni() { return Collections.unmodifiableList(iscrizioni); }
    public List<Sottomissione> getSottomissioni() { return Collections.unmodifiableList(sottomissioni); }
    public HackathonStatus getStato() { return stato; }
    public Team getTeamVincitore() { return teamVincitore; }
}
