package it.hackhub.service;

import it.hackhub.domain.Hackathon;
import it.hackhub.domain.Sottomissione;
import it.hackhub.domain.Team;
import it.hackhub.repository.HackathonRepository;
import it.hackhub.repository.SottomissioneRepository;
import it.hackhub.repository.TeamRepository;
import it.hackhub.service.exception.HackathonNonTrovatoException;
import it.hackhub.service.exception.TeamNonIscrittoException;
import it.hackhub.service.exception.TeamNonTrovatoException;
import org.springframework.stereotype.Service;

@Service
public class SottomissioneService {
    private final SottomissioneRepository sottomissioneRepository;
    private final HackathonRepository hackathonRepository;
    private final TeamRepository teamRepository;

    public SottomissioneService(SottomissioneRepository sottomissioneRepository,
                                HackathonRepository hackathonRepository,
                                TeamRepository teamRepository) {
        this.sottomissioneRepository = sottomissioneRepository;
        this.hackathonRepository = hackathonRepository;
        this.teamRepository = teamRepository;
    }

    public Sottomissione inviaSottomissione(Long teamId, Long hackathonId, String contenuto) {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNonTrovatoException::new);
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow(HackathonNonTrovatoException::new);
        if (!hackathon.verificaGiaIscritto(teamId)) {
            throw new TeamNonIscrittoException();
        }
        if (!hackathon.isInCorso()) {
            throw new IllegalStateException("Invio non possibile: hackathon non in corso");
        }
        team.registraNuovaSottomissione(contenuto);
        Sottomissione sottomissione = new Sottomissione(System.currentTimeMillis(), hackathonId, teamId, contenuto);
        hackathon.associaSottomissione(sottomissione);
        sottomissioneRepository.save(sottomissione);
        hackathonRepository.save(hackathon);
        return sottomissione;
    }
}
