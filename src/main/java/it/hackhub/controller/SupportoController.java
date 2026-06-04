package it.hackhub.controller;

import it.hackhub.domain.CallSupporto;
import it.hackhub.domain.RichiestaSupporto;
import it.hackhub.service.SupportoService;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class SupportoController {
    private final SupportoService supportoService;

    public SupportoController(SupportoService supportoService) {
        this.supportoService = supportoService;
    }

    public RichiestaSupporto inviaDatiRichiesta(Long utenteId, String topic, String descrizione) {
        return supportoService.richiediSupporto(utenteId, topic, descrizione);
    }

    public RichiestaSupporto inviaDatiRichiesta(Long utenteId, Long teamId, Long hackathonId,
                                                String topic, String descrizione, String priorita) {
        return supportoService.richiediSupporto(utenteId, teamId, hackathonId, topic, descrizione, priorita);
    }

    public List<RichiestaSupporto> visualizzaRichiesteAperte() {
        return supportoService.recuperaRichiesteAperte();
    }

    public RichiestaSupporto getDettagliRichiesta(Long richiestaId) {
        return supportoService.getDettagliRichiesta(richiestaId);
    }

    public CallSupporto proponiCallSupporto(Long richiestaId, Long mentoreId, LocalDateTime dataOra) {
        return supportoService.proponiCallSupporto(richiestaId, mentoreId, dataOra);
    }
}
