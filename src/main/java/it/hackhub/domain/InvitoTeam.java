package it.hackhub.domain;

import java.time.LocalDateTime;

public class InvitoTeam {
    private Long id;
    private Long teamId;
    private Long utenteInvitatoId;
    private Long invitanteId;
    private LocalDateTime dataInvio;
    private InvitoTeamStatus stato;

    public InvitoTeam(Long id, Long teamId, Long utenteInvitatoId, Long invitanteId) {
        if (teamId == null || utenteInvitatoId == null || invitanteId == null) {
            throw new IllegalArgumentException("Team, utente invitato e invitante sono obbligatori");
        }
        this.id = id;
        this.teamId = teamId;
        this.utenteInvitatoId = utenteInvitatoId;
        this.invitanteId = invitanteId;
        this.dataInvio = LocalDateTime.now();
        this.stato = InvitoTeamStatus.IN_ATTESA;
    }

    public void accetta() {
        if (stato != InvitoTeamStatus.IN_ATTESA) {
            throw new IllegalStateException("Invito già gestito");
        }
        stato = InvitoTeamStatus.ACCETTATO;
    }

    public void rifiuta() {
        if (stato != InvitoTeamStatus.IN_ATTESA) {
            throw new IllegalStateException("Invito già gestito");
        }
        stato = InvitoTeamStatus.RIFIUTATO;
    }

    public boolean isInAttesa() { return stato == InvitoTeamStatus.IN_ATTESA; }
    public Long getId() { return id; }
    public Long getTeamId() { return teamId; }
    public Long getUtenteInvitatoId() { return utenteInvitatoId; }
    public Long getInvitanteId() { return invitanteId; }
    public LocalDateTime getDataInvio() { return dataInvio; }
    public InvitoTeamStatus getStato() { return stato; }
}
