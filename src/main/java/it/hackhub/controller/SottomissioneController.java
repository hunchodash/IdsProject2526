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

    public List<Sottomissione> consultaSottomissioni(Long teamId, Long hackathonId) {
        return sottomissioneService.consultaSottomissioni(teamId, hackathonId);
    }

    public List<Sottomissione> consultaSottomissioni(Long hackathonId) {
        return sottomissioneService.consultaSottomissioni(hackathonId);
    }

    public List<Sottomissione> consultaSottomissioniStaff(Long hackathonId, Long staffId) {
        return sottomissioneService.consultaSottomissioniStaff(hackathonId, staffId);
    }

    public Sottomissione aggiornaSottomissione(Long sottomissioneId, String nuovoContenuto) {
        return sottomissioneService.aggiornaSottomissione(sottomissioneId, nuovoContenuto);
    }

    public Sottomissione getDettagli(Long sottomissioneId) {
        return sottomissioneService.getDettagli(sottomissioneId);
    }
}
