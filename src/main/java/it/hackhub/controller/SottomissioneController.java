package it.hackhub.controller;

import it.hackhub.domain.Sottomissione;
import it.hackhub.service.SottomissioneService;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SottomissioneController {
    private final SottomissioneService sottomissioneService;

    public SottomissioneController(SottomissioneService sottomissioneService) {
        this.sottomissioneService = sottomissioneService;
    }

    public Sottomissione inviaSottomissione(Long teamId, Long hackathonId, String contenuto) {
        return sottomissioneService.inviaSottomissione(teamId, hackathonId, contenuto);
    }

    public List<Sottomissione> consultaSottomissioni(Long hackathonId) {
        return sottomissioneService.consultaSottomissioni(hackathonId);
    }

    public Sottomissione getDettagli(Long sottomissioneId) {
        return sottomissioneService.getDettagli(sottomissioneId);
    }
}
