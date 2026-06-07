package it.hackhub.controller;

import it.hackhub.domain.InvitoTeam;
import it.hackhub.domain.Team;
import it.hackhub.domain.Utente;
import it.hackhub.service.TeamService;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    public Team creaTeam(String nomeTeam, Utente creatore) {
        return teamService.creaTeam(nomeTeam, creatore);
    }

    public List<Team> visualizzaTeam() {
        return teamService.getTuttiITeam();
    }

    public InvitoTeam invitaUtente(Long teamId, Long invitanteId, String emailInvitato) {
        return teamService.invitaUtente(teamId, invitanteId, emailInvitato);
    }

    public void accettaInvito(Long invitoId, Long utenteId) {
        teamService.accettaInvito(invitoId, utenteId);
    }

    public void rifiutaInvito(Long invitoId, Long utenteId) {
        teamService.rifiutaInvito(invitoId, utenteId);
    }

    public List<InvitoTeam> getInvitiUtente(Long utenteId) {
        return teamService.getInvitiUtente(utenteId);
    }

    public List<Team> getTeamDiUtente(Utente utente) {
        return teamService.getTeamDiUtente(utente);
    }
}
