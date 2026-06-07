package it.hackhub.service;

import it.hackhub.adapter.CalendarAdapter;
import it.hackhub.domain.CallSupporto;
import it.hackhub.domain.Hackathon;
import it.hackhub.domain.RichiestaSupporto;
import it.hackhub.domain.Team;
import it.hackhub.domain.Utente;
import it.hackhub.repository.HackathonRepository;
import it.hackhub.repository.RichiestaSupportoRepository;
import it.hackhub.repository.TeamRepository;
import it.hackhub.repository.UtenteRepository;
import it.hackhub.service.exception.HackathonNonTrovatoException;
import it.hackhub.service.exception.TeamNonTrovatoException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SupportoService {
    private final RichiestaSupportoRepository richiestaSupportoRepository;
    private final CalendarAdapter calendarAdapter;
    private final HackathonRepository hackathonRepository;
    private final TeamRepository teamRepository;
    private final UtenteRepository utenteRepository;

    public SupportoService(RichiestaSupportoRepository richiestaSupportoRepository,
                           CalendarAdapter calendarAdapter,
                           HackathonRepository hackathonRepository,
                           TeamRepository teamRepository,
                           UtenteRepository utenteRepository) {
        this.richiestaSupportoRepository = richiestaSupportoRepository;
        this.calendarAdapter = calendarAdapter;
        this.hackathonRepository = hackathonRepository;
        this.teamRepository = teamRepository;
        this.utenteRepository = utenteRepository;
    }

    public RichiestaSupporto richiediSupporto(Long utenteId, String topic, String descrizione) {
        RichiestaSupporto richiesta = new RichiestaSupporto(System.currentTimeMillis(), utenteId, topic, descrizione);
        richiesta.salvaNelSistema();
        richiestaSupportoRepository.save(richiesta);
        return richiesta;
    }

    public RichiestaSupporto richiediSupporto(Long utenteId, Long teamId, Long hackathonId,
                                              String topic, String descrizione, String priorita) {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNonTrovatoException::new);
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow(HackathonNonTrovatoException::new);
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        if (!team.getMembri().contains(utente)) {
            throw new IllegalStateException("Solo un membro del team può aprire richieste di supporto per quel team");
        }
        if (!hackathon.verificaGiaIscritto(teamId)) {
            throw new IllegalStateException("Il team deve essere iscritto all'hackathon per richiedere supporto");
        }
        if (!hackathon.isInCorso()) {
            throw new IllegalStateException("Il supporto del mentore è disponibile durante lo svolgimento dell'hackathon");
        }

        RichiestaSupporto richiesta = new RichiestaSupporto(System.currentTimeMillis(), utenteId, teamId,
                hackathonId, topic, descrizione, priorita);
        richiesta.salvaNelSistema();
        richiestaSupportoRepository.save(richiesta);
        return richiesta;
    }

    public RichiestaSupporto segnalaTeam(Long mentoreId, Long teamId, Long hackathonId, String motivazione) {
        if (motivazione == null || motivazione.isBlank()) {
            throw new IllegalArgumentException("Motivazione obbligatoria");
        }
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNonTrovatoException::new);
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow(HackathonNonTrovatoException::new);
        verificaMentoreAssegnato(hackathon, mentoreId);
        if (!hackathon.verificaGiaIscritto(team.getId())) {
            throw new IllegalStateException("Il team segnalato deve essere iscritto all'hackathon");
        }

        RichiestaSupporto segnalazione = new RichiestaSupporto(System.currentTimeMillis(), mentoreId, teamId,
                hackathonId, "Segnalazione team", motivazione, "ALTA");
        segnalazione.salvaNelSistema();
        richiestaSupportoRepository.save(segnalazione);
        return segnalazione;
    }

    public List<RichiestaSupporto> recuperaRichiesteAperte() {
        return richiestaSupportoRepository.findAperte();
    }

    public List<RichiestaSupporto> recuperaRichiestePerHackathon(Long hackathonId) {
        hackathonRepository.findById(hackathonId).orElseThrow(HackathonNonTrovatoException::new);
        return richiestaSupportoRepository.findByHackathonId(hackathonId);
    }

    public RichiestaSupporto getDettagliRichiesta(Long richiestaId) {
        return richiestaSupportoRepository.findById(richiestaId)
                .orElseThrow(() -> new RuntimeException("Richiesta di supporto non trovata"));
    }

    public CallSupporto proponiCallSupporto(Long richiestaId, Long mentoreId, LocalDateTime dataOra) {
        if (dataOra == null || dataOra.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La call deve essere pianificata in una data futura");
        }
        RichiestaSupporto richiesta = getDettagliRichiesta(richiestaId);
        if (!richiesta.isAperta()) {
            throw new IllegalStateException("La richiesta di supporto è già chiusa");
        }
        Hackathon hackathon = hackathonRepository.findById(richiesta.getHackathonId())
                .orElseThrow(HackathonNonTrovatoException::new);
        verificaMentoreAssegnato(hackathon, mentoreId);

        String link = calendarAdapter.pianificaCall("Supporto HackHub - " + richiesta.getTopic(), dataOra);
        return new CallSupporto(System.currentTimeMillis(), richiestaId, mentoreId, dataOra, link);
    }

    public void chiudiRichiesta(Long richiestaId) {
        RichiestaSupporto richiesta = getDettagliRichiesta(richiestaId);
        richiesta.chiudi();
        richiestaSupportoRepository.save(richiesta);
    }

    private void verificaMentoreAssegnato(Hackathon hackathon, Long mentoreId) {
        boolean mentoreAssegnato = hackathon.getMentori().stream()
                .anyMatch(m -> m.getId().equals(mentoreId));
        if (!mentoreAssegnato) {
            throw new IllegalStateException("Il mentore può operare solo sugli hackathon a cui è assegnato");
        }
    }
}
