package it.hackhub.service;

import it.hackhub.domain.Hackathon;
import it.hackhub.domain.Sottomissione;
import it.hackhub.repository.HackathonRepository;
import it.hackhub.repository.IscrizioneRepository;
import it.hackhub.repository.SottomissioneRepository;
import it.hackhub.service.exception.HackathonNonInCorsoException;
import it.hackhub.service.exception.HackathonNonTrovatoException;
import it.hackhub.service.exception.TeamNonIscrittoException;
import org.springframework.stereotype.Service;

@Service

public class SubmissionService {
    private final SottomissioneRepository sottomissioneRepository;
    private final HackathonRepository hackathonRepository;
    private final IscrizioneRepository iscrizioneRepository;

    // IL COSTRUTTORE DEVE AVERE QUESTI 3 PARAMETRI
    public SubmissionService(SottomissioneRepository sottomissioneRepository,
                             HackathonRepository hackathonRepository,
                             IscrizioneRepository iscrizioneRepository) {
        this.sottomissioneRepository = sottomissioneRepository;
        this.hackathonRepository = hackathonRepository;
        this.iscrizioneRepository = iscrizioneRepository;
    }

    public void inviaSottomissione(Long hackathonId, Long teamId, String contenuto) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId)
                .orElseThrow(HackathonNonTrovatoException::new);

        if (!hackathon.getStato().puoInviareSottomissione()) {
            throw new HackathonNonInCorsoException();
        }

        if (!iscrizioneRepository.existsByHackathonIdAndTeamId(hackathonId, teamId)) {
            throw new TeamNonIscrittoException();
        }

        Sottomissione sottomissione = new Sottomissione(System.currentTimeMillis(), hackathonId, teamId, contenuto);
        sottomissioneRepository.save(sottomissione);
    }
}