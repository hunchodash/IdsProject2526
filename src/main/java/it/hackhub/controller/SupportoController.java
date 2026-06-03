package it.hackhub.controller;

import it.hackhub.domain.RichiestaSupporto;
import it.hackhub.service.SupportoService;
import org.springframework.stereotype.Component;

@Component
public class SupportoController {
    private final SupportoService supportoService;

    public SupportoController(SupportoService supportoService) {
        this.supportoService = supportoService;
    }

    public RichiestaSupporto inviaDatiRichiesta(Long utenteId, String topic, String descrizione) {
        return supportoService.richiediSupporto(utenteId, topic, descrizione);
    }
}
