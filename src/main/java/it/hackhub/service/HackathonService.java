package it.hackhub.service;

import it.hackhub.adapter.PaymentAdapter;
import it.hackhub.domain.ClassificaEntry;
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
import it.hackhub.strategy.WinnerSelectionStrategy;
import it.hackhub.service.exception.HackathonNonTrovatoException;
import it.hackhub.service.exception.TeamNonTrovatoException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HackathonService {

    private final HackathonRepository hackathonRepository;
    private final UtenteRepository utenteRepository;
    private final TeamRepository teamRepository;
    private final SottomissioneRepository sottomissioneRepository;
    private final ValutazioneRepository valutazioneRepository;
    private final PaymentAdapter paymentAdapter;
    private final WinnerSelectionStrategy winnerSelectionStrategy;

    public HackathonService(HackathonRepository hackathonRepository,
                            UtenteRepository utenteRepository,
                            TeamRepository teamRepository,
                            SottomissioneRepository sottomissioneRepository,
                            ValutazioneRepository valutazioneRepository,
                            PaymentAdapter paymentAdapter,
                            WinnerSelectionStrategy winnerSelectionStrategy) {
        this.hackathonRepository = hackathonRepository;
        this.utenteRepository = utenteRepository;
        this.teamRepository = teamRepository;
        this.sottomissioneRepository = sottomissioneRepository;
        this.valutazioneRepository = valutazioneRepository;
        this.paymentAdapter = paymentAdapter;
        this.winnerSelectionStrategy = winnerSelectionStrategy;
    }

    public Hackathon creaHackathon(String nome,
                                   String regolamento,
                                   LocalDateTime scadenzaIscrizioni,
                                   LocalDateTime dataInizio,
                                   LocalDateTime dataFine,
                                   String luogo,
                                   double premioInDenaro,
                                   int maxTeamSize,
                                   Organizzatore organizzatore,
                                   Giudice giudice,
                                   List<Mentore> mentori) {
        if (organizzatore == null) {
            throw new IllegalArgumentException("Organizzatore obbligatorio");
        }

        if (giudice == null) {
            throw new IllegalArgumentException("Un hackathon deve avere un giudice assegnato");
        }

        if (mentori == null || mentori.isEmpty()) {
            throw new IllegalArgumentException("Un hackathon deve avere almeno un mentore assegnato");
        }

        Hackathon hackathon = new Hackathon(
                System.currentTimeMillis(),
                nome,
                regolamento,
                scadenzaIscrizioni,
                dataInizio,
                dataFine,
                luogo,
                premioInDenaro,
                maxTeamSize,
                organizzatore
        );

        hackathon.aggiungiGiudice(giudice);
        mentori.forEach(hackathon::aggiungiMentore);

        hackathon.salvaNelSistema();
        hackathonRepository.save(hackathon);

        return hackathon;
    }

    public void aggiungiMentore(Long hackathonId, Mentore mentore) {
        Hackathon hackathon = getHackathon(hackathonId);

        if (mentore == null) {
            throw new IllegalArgumentException("Mentore obbligatorio");
        }

        hackathon.aggiungiMentore(mentore);
        hackathonRepository.save(hackathon);
    }

    public void aggiungiGiudice(Long hackathonId, Giudice giudice) {
        Hackathon hackathon = getHackathon(hackathonId);

        if (giudice == null) {
            throw new IllegalArgumentException("Giudice obbligatorio");
        }

        hackathon.aggiungiGiudice(giudice);
        hackathonRepository.save(hackathon);
    }

    public void assegnaStaff(Long hackathonId, String email, String ruolo) {
        Hackathon hackathon = getHackathon(hackathonId);

        Utente utente = utenteRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utente non esistente"));

        if (!(utente instanceof MembroStaff)) {
            throw new IllegalArgumentException("L'utente non è un membro dello staff");
        }

        hackathon.aggiungiStaff((MembroStaff) utente, ruolo);
        hackathonRepository.save(hackathon);
    }

    public Team proclamaVincitore(Long hackathonId, Long teamVincitoreId) {
        Hackathon hackathon = getHackathon(hackathonId);
        hackathon.aggiornaStatoCorrente();

        if (hackathon.getStato() == HackathonStatus.CONCLUSO) {
            throw new IllegalStateException("Hackathon già concluso");
        }

        verificaSottomissioniValutate(hackathonId);

        Team vincitore = teamRepository.findById(teamVincitoreId)
                .orElseThrow(TeamNonTrovatoException::new);

        if (!hackathon.verificaGiaIscritto(teamVincitoreId)) {
            throw new IllegalStateException("Il team vincitore deve essere iscritto all'hackathon");
        }

        Sottomissione sottomissioneVincitore = sottomissioneRepository
                .findByHackathonIdAndTeamId(hackathonId, teamVincitoreId)
                .orElseThrow(() -> new IllegalStateException("Il team vincitore deve avere una sottomissione"));

        if (valutazioneRepository.findBySottomissioneId(sottomissioneVincitore.getId()).isEmpty()) {
            throw new IllegalStateException("La sottomissione del team vincitore deve essere valutata");
        }

        hackathon.setVincitore(vincitore);

        boolean premioErogato = paymentAdapter.erogaPremio(
                vincitore,
                hackathon.getPremioInDenaro()
        );

        if (!premioErogato) {
            throw new IllegalStateException("Premio non erogato");
        }

        hackathon.setStato(HackathonStatus.CONCLUSO);
        hackathonRepository.save(hackathon);

        return vincitore;
    }

    public Team proclamaVincitoreAutomaticamente(Long hackathonId) {
        List<ClassificaEntry> classifica = calcolaClassifica(hackathonId);

        if (classifica.isEmpty()) {
            throw new IllegalStateException("Impossibile proclamare un vincitore: classifica vuota");
        }

        ClassificaEntry vincitore = winnerSelectionStrategy.selezionaVincitore(classifica);

        return proclamaVincitore(hackathonId, vincitore.getTeam().getId());
    }

    public List<ClassificaEntry> calcolaClassifica(Long hackathonId) {
        getHackathon(hackathonId);

        return sottomissioneRepository.findByHackathonId(hackathonId).stream()
                .filter(s -> !valutazioneRepository.findBySottomissioneId(s.getId()).isEmpty())
                .map(s -> new ClassificaEntry(
                        teamRepository.findById(s.getTeamId())
                                .orElseThrow(TeamNonTrovatoException::new),
                        s,
                        valutazioneRepository.findBySottomissioneId(s.getId()).stream()
                                .mapToInt(v -> v.getPunteggio())
                                .average()
                                .orElse(0.0)
                ))
                .sorted(Comparator.comparingDouble(ClassificaEntry::getPunteggioMedio).reversed())
                .collect(Collectors.toList());
    }

    public void verificaSottomissioniValutate(Long hackathonId) {
        getHackathon(hackathonId);

        List<Sottomissione> sottomissioni = sottomissioneRepository.findByHackathonId(hackathonId);

        if (sottomissioni.isEmpty()) {
            throw new IllegalStateException("Nessuna sottomissione presente");
        }

        boolean tutteValutate = sottomissioni.stream()
                .allMatch(s -> !valutazioneRepository.findBySottomissioneId(s.getId()).isEmpty());

        if (!tutteValutate) {
            throw new IllegalStateException("Il giudice non ha ancora valutato tutte le sottomissioni");
        }
    }

    public List<Hackathon> consultaHackathonAssegnati(Long staffId) {
        return hackathonRepository.findAll().stream()
                .filter(h -> h.getGiudici().stream().anyMatch(g -> g.getId().equals(staffId))
                        || h.getMentori().stream().anyMatch(m -> m.getId().equals(staffId))
                        || (h.getOrganizzatore() != null && h.getOrganizzatore().getId().equals(staffId)))
                .collect(Collectors.toList());
    }

    public List<Hackathon> consultaHackathonInValutazione(Long giudiceId) {
        return hackathonRepository.findAll().stream()
                .peek(Hackathon::aggiornaStatoCorrente)
                .filter(h -> h.getGiudici().stream().anyMatch(g -> g.getId().equals(giudiceId)))
                .filter(h -> h.getStato() == HackathonStatus.VALUTAZIONE)
                .collect(Collectors.toList());
    }

    public Hackathon getHackathon(Long hackathonId) {
        return hackathonRepository.findById(hackathonId)
                .orElseThrow(HackathonNonTrovatoException::new);
    }

    public List<Hackathon> consultaHackathon() {
        hackathonRepository.findAll().forEach(Hackathon::aggiornaStatoCorrente);
        return hackathonRepository.findAll();
    }
}