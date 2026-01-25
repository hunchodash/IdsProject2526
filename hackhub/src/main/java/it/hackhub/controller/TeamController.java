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
            return "OK: Iscrizione completata con successo!";
        } catch (Exception e) {
            return "ERRORE: " + e.getMessage();
        }
    }
}