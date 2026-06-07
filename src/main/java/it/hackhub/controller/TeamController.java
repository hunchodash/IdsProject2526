package it.hackhub.controller;

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

    public List<Team> getTeamDiUtente(Utente utente) {
        return teamService.getTeamDiUtente(utente);
    }
}
