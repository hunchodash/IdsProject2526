package it.hackhub.service;

import it.hackhub.domain.*;
import it.hackhub.repository.*;
import it.hackhub.service.exception.*;

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
        // 1. Recupero entità tramite le variabili di istanza (minuscole!)
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(HackathonNonTrovatoException::new);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNonTrovatoException::new);

        // 2. Controllo Stato tramite Polimorfismo (senza instanceof)
        if (!hackathon.getStato().puoEffettuareIscrizione()) {
            throw new HackathonNonInIscrizioneException();
        }

        // 3. Controllo duplicati
        if (iscrizioneRepository.existsByHackathonIdAndTeamId(hackathonId, teamId)) {
            throw new TeamGiaIscrittoException();
        }

        // 4. Delega della validazione al dominio
        if (!hackathon.isTeamSizeValida(team.getNumeroMembri())) {
            throw new TeamTroppoGrandeException();
        }

        // 5. Salvataggio
        iscrizioneRepository.save(new Iscrizione(hackathonId, teamId));
    }
}