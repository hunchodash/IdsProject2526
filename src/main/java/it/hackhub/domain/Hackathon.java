package it.hackhub.domain;

import it.hackhub.domain.staff.Organizzatore;
import it.hackhub.domain.staff.Giudice;
import it.hackhub.domain.staff.Mentore;
import it.hackhub.domain.state.StatoHackathon;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private StatoHackathon stato;
    private Organizzatore organizzatore;
    private Giudice giudice;
    private List<Mentore> mentori;
    private List<Team> teamIscritti;


    public Hackathon(Long id, String nome, String regolamento,
                     LocalDateTime scadenzaIscrizioni, LocalDateTime dataInizio,
                     LocalDateTime dataFine, String luogo, double premioInDenaro,
                     int maxTeamSize, StatoHackathon stato,
                     Organizzatore organizzatore, Giudice giudice) {
        this.id = id;
        this.nome = nome;
        this.regolamento = regolamento;
        this.scadenzaIscrizioni = scadenzaIscrizioni;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.luogo = luogo;
        this.premioInDenaro = premioInDenaro;
        this.maxTeamSize = maxTeamSize;
        this.stato = stato;
        this.organizzatore = organizzatore;
        this.giudice = giudice;
        this.mentori = new ArrayList<>();
        this.teamIscritti = new ArrayList<>();
    }


    public Hackathon(Long id, String nome, int maxTeamSize, StatoHackathon stato,
                     Organizzatore organizzatore, Giudice giudice) {
        this(id, nome, "Regolamento di default",
                LocalDateTime.now().plusDays(30),
                LocalDateTime.now().plusDays(40),
                LocalDateTime.now().plusDays(47),
                "Online", 1000.0, maxTeamSize, stato, organizzatore, giudice);
    }


    public void aggiungiMentore(Mentore mentore) {
        if (!mentori.contains(mentore)) {
            this.mentori.add(mentore);
        }
    }


    public void iscriviTeam(Team team) {
        if (!teamIscritti.contains(team)) {
            this.teamIscritti.add(team);
        }
    }


    public boolean isTeamIscritto(Team team) {
        return teamIscritti.contains(team);
    }


    public boolean isIscrizioniAperte() {
        return LocalDateTime.now().isBefore(scadenzaIscrizioni);
    }


    public boolean isInCorso() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(dataInizio) && now.isBefore(dataFine);
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
    public StatoHackathon getStato() { return stato; }
    public Organizzatore getOrganizzatore() { return organizzatore; }
    public Giudice getGiudice() { return giudice; }
    public List<Mentore> getMentori() { return mentori; }
    public List<Team> getTeamIscritti() { return teamIscritti; }


    public void setStato(StatoHackathon stato) { this.stato = stato; }
    public void setRegolamento(String regolamento) { this.regolamento = regolamento; }
    public void setLuogo(String luogo) { this.luogo = luogo; }
    public void setPremioInDenaro(double premioInDenaro) { this.premioInDenaro = premioInDenaro; }


    public boolean isTeamSizeValida(int size) {
        return size > 0 && size <= this.maxTeamSize;
    }

    @Override
    public String toString() {
        return "Hackathon: " + nome + " [Stato: " + stato.getNome() + "]";
    }
}