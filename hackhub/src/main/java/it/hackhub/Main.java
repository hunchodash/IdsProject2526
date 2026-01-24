package it.hackhub;

import it.hackhub.controller.TeamController;
import it.hackhub.domain.*;
import it.hackhub.domain.state.*;
import it.hackhub.repository.memory.*;
import it.hackhub.service.TeamService;

public class Main {
    public static void main(String[] args) {
        // Setup Repositories
        InMemoryHackathonRepository hackathonRepo = new InMemoryHackathonRepository();
        InMemoryTeamRepository teamRepo = new InMemoryTeamRepository();
        InMemoryIscrizioneRepository iscrizioneRepo = new InMemoryIscrizioneRepository();

        TeamService teamService = new TeamService(hackathonRepo, teamRepo, iscrizioneRepo);
        TeamController teamController = new TeamController(teamService);

        // 1. Creazione Hackathon (Max 4 persone) e Team (3 persone)
        Hackathon hackathon = new Hackathon(1L, 4, new IscrizioneAperta());
        hackathonRepo.save(hackathon);
        Team teamOk = new Team(10L, 3);
        teamRepo.save(teamOk);

        // Test 1: Iscrizione corretta
        System.out.println(teamController.iscriviTeam(10L, 1L));

        // Test 2: Doppio iscrizione (deve dare errore)
        System.out.println(teamController.iscriviTeam(10L, 1L));

        // Test 3: Stato sbagliato
        hackathon.setStato(new InCorso());
        Team teamLate = new Team(11L, 2);
        teamRepo.save(teamLate);
        System.out.println(teamController.iscriviTeam(11L, 1L));
    }
}