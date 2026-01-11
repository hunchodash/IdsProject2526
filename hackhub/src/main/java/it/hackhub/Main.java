package it.hackhub;

import it.hackhub.controller.TeamController;
import it.hackhub.domain.Hackathon;
import it.hackhub.domain.Team;
import it.hackhub.domain.state.InCorso;
import it.hackhub.domain.state.IscrizioneAperta;
import it.hackhub.repository.memory.InMemoryHackathonRepository;
import it.hackhub.repository.memory.InMemoryIscrizioneRepository;
import it.hackhub.repository.memory.InMemoryTeamRepository;
import it.hackhub.service.TeamService;

public class Main {
    public static void main(String[] args) {
        InMemoryHackathonRepository hackathonRepo = new InMemoryHackathonRepository();
        InMemoryTeamRepository teamRepo = new InMemoryTeamRepository();
        InMemoryIscrizioneRepository iscrizioneRepo = new InMemoryIscrizioneRepository();

        TeamService teamService = new TeamService(hackathonRepo, teamRepo, iscrizioneRepo);
        TeamController teamController = new TeamController(teamService);

        Hackathon hackathon = new Hackathon(1L, 4, new IscrizioneAperta());
        hackathonRepo.save(hackathon);

        Team team = new Team(10L, 3);
        teamRepo.save(team);

        System.out.println(teamController.iscriviTeam(10L, 1L));
        System.out.println(teamController.iscriviTeam(10L, 1L));

        hackathon.setStato(new InCorso());
        System.out.println(teamController.iscriviTeam(10L, 1L));
    }
}
