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
import java.util.List;

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

    public List<Sottomissione> consultaSottomissioni(Long teamId, Long hackathonId) {
        if (!teamRepository.findById(teamId).isPresent()) {
            throw new TeamNonTrovatoException();
        }
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow(HackathonNonTrovatoException::new);
        if (!hackathon.verificaGiaIscritto(teamId)) {
            throw new TeamNonIscrittoException();
        }
        return sottomissioneRepository.findByHackathonId(hackathonId).stream()
                .filter(s -> s.getTeamId().equals(teamId))
                .toList();
    }

    public List<Sottomissione> consultaSottomissioni(Long hackathonId) {
        return sottomissioneRepository.findByHackathonId(hackathonId);
    }

    public List<Sottomissione> consultaSottomissioniStaff(Long hackathonId, Long staffId) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow(HackathonNonTrovatoException::new);
        boolean assegnato = hackathon.getGiudici().stream().anyMatch(g -> g.getId().equals(staffId))
                || hackathon.getMentori().stream().anyMatch(m -> m.getId().equals(staffId))
                || (hackathon.getOrganizzatore() != null && hackathon.getOrganizzatore().getId().equals(staffId));
        if (!assegnato) {
            throw new IllegalStateException("Lo staff può consultare solo sottomissioni di hackathon assegnati");
        }
        return sottomissioneRepository.findByHackathonId(hackathonId);
    }

    public Sottomissione aggiornaSottomissione(Long sottomissioneId, String nuovoContenuto) {
        Sottomissione sottomissione = getDettagli(sottomissioneId);
        Hackathon hackathon = hackathonRepository.findById(sottomissione.getHackathonId()).orElseThrow(HackathonNonTrovatoException::new);
        if (!hackathon.isInCorso()) {
            throw new IllegalStateException("Aggiornamento non possibile: hackathon non in corso");
        }
        sottomissione.aggiornaContenuto(nuovoContenuto);
        sottomissioneRepository.save(sottomissione);
        return sottomissione;
    }

    public Sottomissione getDettagli(Long sottomissioneId) {
        return sottomissioneRepository.findById(sottomissioneId)
                .orElseThrow(() -> new RuntimeException("Sottomissione non trovata"));
    }
}
