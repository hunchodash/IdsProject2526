package it.hackhub.service;

import it.hackhub.domain.Hackathon;
import it.hackhub.domain.Team;
import it.hackhub.domain.Utente;
import it.hackhub.repository.HackathonRepository;
import it.hackhub.repository.TeamRepository;
import it.hackhub.service.exception.HackathonNonTrovatoException;
import it.hackhub.service.exception.TeamNonTrovatoException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final HackathonRepository hackathonRepository;

    public TeamService(TeamRepository teamRepository, HackathonRepository hackathonRepository) {
        this.teamRepository = teamRepository;
        this.hackathonRepository = hackathonRepository;
    }

    public Team creaTeam(String nomeTeam, Utente creatore) {
        if (nomeTeam == null || nomeTeam.isBlank()) {
            throw new IllegalArgumentException("Nome team obbligatorio");
        }
        Team team = new Team(System.currentTimeMillis(), nomeTeam);
        team.aggiungiMembro(creatore);
        teamRepository.save(team);
        return team;
    }

    public void iscriviTeam(Long teamId, Long hackathonId) {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNonTrovatoException::new);
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow(HackathonNonTrovatoException::new);
        hackathon.iscriviTeam(team);
        hackathonRepository.save(hackathon);
    }

    public List<Team> getTeamDiUtente(Utente utente) {
        return teamRepository.findByMembro(utente);
    }
}
