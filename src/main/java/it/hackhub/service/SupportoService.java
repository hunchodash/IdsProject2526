package it.hackhub.service;

import it.hackhub.adapter.CalendarAdapter;
import it.hackhub.domain.CallSupporto;
import it.hackhub.domain.RichiestaSupporto;
import it.hackhub.repository.RichiestaSupportoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SupportoService {
    private final RichiestaSupportoRepository richiestaSupportoRepository;
    private final CalendarAdapter calendarAdapter;

    public SupportoService(RichiestaSupportoRepository richiestaSupportoRepository,
                           CalendarAdapter calendarAdapter) {
        this.richiestaSupportoRepository = richiestaSupportoRepository;
        this.calendarAdapter = calendarAdapter;
    }

    public RichiestaSupporto richiediSupporto(Long utenteId, String topic, String descrizione) {
        RichiestaSupporto richiesta = new RichiestaSupporto(System.currentTimeMillis(), utenteId, topic, descrizione);
        richiesta.salvaNelSistema();
        richiestaSupportoRepository.save(richiesta);
        return richiesta;
    }

    public RichiestaSupporto richiediSupporto(Long utenteId, Long teamId, Long hackathonId,
                                              String topic, String descrizione, String priorita) {
        RichiestaSupporto richiesta = new RichiestaSupporto(System.currentTimeMillis(), utenteId, teamId,
                hackathonId, topic, descrizione, priorita);
        richiesta.salvaNelSistema();
        richiestaSupportoRepository.save(richiesta);
        return richiesta;
    }

    public List<RichiestaSupporto> recuperaRichiesteAperte() {
        return richiestaSupportoRepository.findAperte();
    }

    public List<RichiestaSupporto> recuperaRichiestePerHackathon(Long hackathonId) {
        return richiestaSupportoRepository.findByHackathonId(hackathonId);
    }

    public RichiestaSupporto getDettagliRichiesta(Long richiestaId) {
        return richiestaSupportoRepository.findById(richiestaId)
                .orElseThrow(() -> new RuntimeException("Richiesta di supporto non trovata"));
    }

    public CallSupporto proponiCallSupporto(Long richiestaId, Long mentoreId, LocalDateTime dataOra) {
        RichiestaSupporto richiesta = getDettagliRichiesta(richiestaId);
        String link = calendarAdapter.pianificaCall("Supporto HackHub - " + richiesta.getTopic(), dataOra);
        return new CallSupporto(System.currentTimeMillis(), richiestaId, mentoreId, dataOra, link);
    }

    public void chiudiRichiesta(Long richiestaId) {
        RichiestaSupporto richiesta = getDettagliRichiesta(richiestaId);
        richiesta.chiudi();
        richiestaSupportoRepository.save(richiesta);
    }
}
