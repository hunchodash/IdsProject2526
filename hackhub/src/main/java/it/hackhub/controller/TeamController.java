package it.hackhub.controller;

import it.hackhub.service.TeamService;

public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    public String iscriviTeam(Long teamId, Long hackathonId) {
        try {
            teamService.iscriviTeam(teamId, hackathonId);
            return "Iscrizione completata correttamente";
        } catch (RuntimeException e) {
            return "Errore: " + e.getMessage();
        }
    }
}
