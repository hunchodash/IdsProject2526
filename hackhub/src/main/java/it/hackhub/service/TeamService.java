package it.hackhub.service;

import it.hackhub.domain.Hackathon;
import it.hackhub.domain.Iscrizione;
import it.hackhub.domain.Team;
import it.hackhub.domain.state.IscrizioneAperta;
import it.hackhub.repository.HackathonRepository;
import it.hackhub.repository.IscrizioneRepository;
import it.hackhub.repository.TeamRepository;
import it.hackhub.service.exception.HackathonNonInIscrizioneException;
import it.hackhub.service.exception.HackathonNonTrovatoException;
import it.hackhub.service.exception.TeamGiaIscrittoException;
import it.hackhub.service.exception.TeamNonTrovatoException;

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

        if (!(hackathon.getStato() instanceof IscrizioneAperta)) {
            throw new HackathonNonInIscrizioneException();
        }

        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNonTrovatoException::new);

        if (iscrizioneRepository.existsByHackathonIdAndTeamId(hackathonId, teamId)) {
            throw new TeamGiaIscrittoException();
        }

        if (team.getNumeroMembri() > hackathon.getMaxTeamSize()) {
            throw new IllegalArgumentException("Team troppo grande per questo hackathon");
        }

        iscrizioneRepository.save(new Iscrizione(hackathonId, teamId));
    }
}
