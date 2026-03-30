package it.hackhub.service;

import it.hackhub.domain.*;
import it.hackhub.repository.*;
import it.hackhub.service.exception.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    private final HackathonRepository hackathonRepository;
    private final TeamRepository teamRepository;
    private final IscrizioneRepository iscrizioneRepository;

    public TeamService(HackathonRepository hackathonRepository,
                       TeamRepository teamRepository,
                       IscrizioneRepository iscrizioneRepository) {
        this.hackathonRepository = hackathonRepository;
        this.teamRepository = teamRepository;
        this.iscrizioneRepository = iscrizioneRepository;
    }

    public void iscriviTeam(Long teamId, Long hackathonId) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(HackathonNonTrovatoException::new);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNonTrovatoException::new);

        if (!hackathon.getStato().puoEffettuareIscrizione()) {
            throw new HackathonNonInIscrizioneException();
        }

        if (iscrizioneRepository.existsByHackathonIdAndTeamId(hackathonId, teamId)) {
            throw new TeamGiaIscrittoException();
        }

        if (!hackathon.isTeamSizeValida(team.getNumeroMembri())) {
            throw new TeamTroppoGrandeException();
        }

        iscrizioneRepository.save(new Iscrizione(hackathonId, teamId));
    }

    public void creaTeam(String nomeTeam, Utente creatore) {
        Long nuovoId = System.currentTimeMillis();
        Team nuovoTeam = new Team(nuovoId, nomeTeam);
        nuovoTeam.aggiungiMembro(creatore);
        teamRepository.save(nuovoTeam);
    }

    public List<Team> getTeamDiUtente(Utente utente) {
        return teamRepository.findByMembro(utente);
    }

    public List<Iscrizione> getIscrizioniTeam(Long teamId) {
        return iscrizioneRepository.findByTeamId(teamId);
    }

    public Optional<Team> getTeamById(Long teamId) {
        return teamRepository.findById(teamId);
    }

    public void salvaTeam(Team team) {
        teamRepository.save(team);
    }
}