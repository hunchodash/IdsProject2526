package it.hackhub.service;

import it.hackhub.adapter.PaymentAdapter;
import it.hackhub.domain.Hackathon;
import it.hackhub.domain.HackathonStatus;
import it.hackhub.domain.Sottomissione;
import it.hackhub.domain.Team;
import it.hackhub.domain.Utente;
import it.hackhub.domain.staff.Giudice;
import it.hackhub.domain.staff.MembroStaff;
import it.hackhub.domain.staff.Mentore;
import it.hackhub.domain.staff.Organizzatore;
import it.hackhub.repository.HackathonRepository;
import it.hackhub.repository.SottomissioneRepository;
import it.hackhub.repository.TeamRepository;
import it.hackhub.repository.UtenteRepository;
import it.hackhub.repository.ValutazioneRepository;
import it.hackhub.service.exception.HackathonNonTrovatoException;
import it.hackhub.service.exception.TeamNonTrovatoException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class HackathonService {
    private final HackathonRepository hackathonRepository;
    private final UtenteRepository utenteRepository;
    private final TeamRepository teamRepository;
    private final SottomissioneRepository sottomissioneRepository;
    private final ValutazioneRepository valutazioneRepository;
    private final PaymentAdapter paymentAdapter;

    public HackathonService(HackathonRepository hackathonRepository,
                            UtenteRepository utenteRepository,
                            TeamRepository teamRepository,
                            SottomissioneRepository sottomissioneRepository,
                            ValutazioneRepository valutazioneRepository,
                            PaymentAdapter paymentAdapter) {
        this.hackathonRepository = hackathonRepository;
        this.utenteRepository = utenteRepository;
        this.teamRepository = teamRepository;
        this.sottomissioneRepository = sottomissioneRepository;
        this.valutazioneRepository = valutazioneRepository;
        this.paymentAdapter = paymentAdapter;
    }

    public Hackathon creaHackathon(String nome, String regolamento,
                                   LocalDateTime scadenzaIscrizioni,
                                   LocalDateTime dataInizio,
                                   LocalDateTime dataFine,
                                   int maxTeamSize,
                                   Organizzatore organizzatore,
                                   Giudice giudice) {
        Hackathon hackathon = creaHackathon(nome, regolamento, scadenzaIscrizioni, dataInizio,
                dataFine, maxTeamSize, organizzatore);
        if (giudice != null) {
            hackathon.aggiungiGiudice(giudice);
            hackathonRepository.save(hackathon);
        }
        return hackathon;
    }

    public Hackathon creaHackathon(String nome, String regolamento,
                                   LocalDateTime scadenzaIscrizioni,
                                   LocalDateTime dataInizio,
                                   LocalDateTime dataFine,
                                   int maxTeamSize,
                                   Organizzatore organizzatore) {
        Hackathon hackathon = new Hackathon(System.currentTimeMillis(), nome, regolamento,
                scadenzaIscrizioni, dataInizio, dataFine, maxTeamSize, organizzatore);
        hackathon.salvaNelSistema();
        hackathonRepository.save(hackathon);
        return hackathon;
    }

    public void aggiungiMentore(Long hackathonId, Mentore mentore) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow(HackathonNonTrovatoException::new);
        hackathon.aggiungiMentore(mentore);
        hackathonRepository.save(hackathon);
    }

    public void aggiungiGiudice(Long hackathonId, Giudice giudice) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow(HackathonNonTrovatoException::new);
        hackathon.aggiungiGiudice(giudice);
        hackathonRepository.save(hackathon);
    }

    public void assegnaStaff(Long hackathonId, String email, String ruolo) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow(HackathonNonTrovatoException::new);
        Utente utente = utenteRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utente non esistente"));
        if (!(utente instanceof MembroStaff)) {
            throw new IllegalArgumentException("L'utente non è un membro dello staff");
        }
        hackathon.aggiungiStaff((MembroStaff) utente, ruolo);
        hackathonRepository.save(hackathon);
    }

    public Team proclamaVincitore(Long hackathonId, Long teamVincitoreId) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow(HackathonNonTrovatoException::new);
        List<Sottomissione> sottomissioni = sottomissioneRepository.findByHackathonId(hackathonId);
        if (sottomissioni.isEmpty()) {
            throw new IllegalStateException("Nessuna sottomissione presente");
        }
        boolean tutteValutate = sottomissioni.stream()
                .allMatch(s -> !valutazioneRepository.findBySottomissioneId(s.getId()).isEmpty());
        if (!tutteValutate) {
            throw new IllegalStateException("Il giudice non ha ancora valutato tutte le sottomissioni");
        }
        Team vincitore = teamRepository.findById(teamVincitoreId).orElseThrow(TeamNonTrovatoException::new);
        hackathon.setVincitore(vincitore);
        hackathon.setStato(HackathonStatus.CONCLUSO);
        hackathonRepository.save(hackathon);
        paymentAdapter.erogaPremio(vincitore, 0.0);
        return vincitore;
    }

    public Team proclamaVincitoreAutomaticamente(Long hackathonId) {
        List<Sottomissione> sottomissioni = sottomissioneRepository.findByHackathonId(hackathonId);
        Sottomissione migliore = sottomissioni.stream()
                .filter(s -> !valutazioneRepository.findBySottomissioneId(s.getId()).isEmpty())
                .max(Comparator.comparingDouble(s -> valutazioneRepository.findBySottomissioneId(s.getId())
                        .stream().mapToInt(v -> v.getPunteggio()).average().orElse(0.0)))
                .orElseThrow(() -> new IllegalStateException("Nessuna sottomissione valutata"));
        return proclamaVincitore(hackathonId, migliore.getTeamId());
    }

    public List<Hackathon> consultaHackathon() { return hackathonRepository.findAll(); }
}
