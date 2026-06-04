package it.hackhub.controller;

import it.hackhub.domain.Valutazione;
import it.hackhub.service.ValutazioneService;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ValutazioneController {
    private final ValutazioneService valutazioneService;

    public ValutazioneController(ValutazioneService valutazioneService) {
        this.valutazioneService = valutazioneService;
    }

    public Valutazione inviaValutazione(Long sottomissioneId, int voto, String commento) {
        return valutazioneService.valutaSottomissione(sottomissioneId, voto, commento);
    }

    public Valutazione inviaValutazione(Long sottomissioneId, Long giudiceId, int voto, String commento) {
        return valutazioneService.valutaSottomissione(sottomissioneId, giudiceId, voto, commento);
    }

    public List<Valutazione> getValutazioniSottomissione(Long sottomissioneId) {
        return valutazioneService.getValutazioniSottomissione(sottomissioneId);
    }
}
