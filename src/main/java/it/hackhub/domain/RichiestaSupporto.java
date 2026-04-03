package it.hackhub.domain;

import it.hackhub.domain.staff.Mentore;

public class RichiestaSupporto {
    private Long id;
    private Long teamId;
    private String nomeTeam;
    private Mentore mentoreAssegnato;
    private boolean gestita;

    public RichiestaSupporto(Long id, Long teamId, String nomeTeam, Mentore mentoreAssegnato) {
        this.id = id;
        this.teamId = teamId;
        this.nomeTeam = nomeTeam;
        this.mentoreAssegnato = mentoreAssegnato;
        this.gestita = false;
    }

    // Getter e Setter
    public Long getId() { return id; }
    public Long getTeamId() { return teamId; }
    public String getNomeTeam() { return nomeTeam; }
    public Mentore getMentoreAssegnato() { return mentoreAssegnato; }
    public boolean isGestita() { return gestita; }

    public void setGestita(boolean gestita) { this.gestita = gestita; }
}