package it.hackhub.controller;

import it.hackhub.domain.Team;
import it.hackhub.domain.Utente;
import it.hackhub.service.TeamService;
import it.hackhub.service.UtenteService;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ProfiloController {
    private final UtenteService utenteService;
    private final TeamService teamService;

    public ProfiloController(UtenteService utenteService, TeamService teamService) {
        this.utenteService = utenteService;
        this.teamService = teamService;
    }

    public Utente aggiornaProfilo(Long utenteId, String nuovoNome, String nuovaEmail) {
        return utenteService.aggiornaProfilo(utenteId, nuovoNome, nuovaEmail);
    }

    public Utente recuperaProfilo(Long utenteId) {
        return utenteService.recuperaUtente(utenteId);
    }

    public List<Team> recuperaTeamIscritti(Utente utente) {
        return teamService.getTeamDiUtente(utente);
    }
}
