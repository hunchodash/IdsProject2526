package it.hackhub.controller;

import it.hackhub.service.TeamService;
import org.springframework.stereotype.Component;

@Component
public class IscrizioneController {
    private final TeamService teamService;

    public IscrizioneController(TeamService teamService) {
        this.teamService = teamService;
    }

    public String iscriviTeam(Long teamId, Long hackathonId) {
        try {
            teamService.iscriviTeam(teamId, hackathonId);
            return "Iscrizione completata con successo";
        } catch (RuntimeException e) {
            return "Errore: " + e.getMessage();
        }
    }
}
