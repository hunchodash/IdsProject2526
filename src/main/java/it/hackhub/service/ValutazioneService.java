package it.hackhub.service;

import it.hackhub.domain.Sottomissione;
import it.hackhub.domain.Valutazione;
import it.hackhub.repository.SottomissioneRepository;
import it.hackhub.repository.ValutazioneRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ValutazioneService {
    private final SottomissioneRepository sottomissioneRepository;
    private final ValutazioneRepository valutazioneRepository;

    public ValutazioneService(SottomissioneRepository sottomissioneRepository,
                              ValutazioneRepository valutazioneRepository) {
        this.sottomissioneRepository = sottomissioneRepository;
        this.valutazioneRepository = valutazioneRepository;
    }

    public Valutazione valutaSottomissione(Long sottomissioneId, int punteggio, String commento) {
        Sottomissione sottomissione = sottomissioneRepository.findById(sottomissioneId)
                .orElseThrow(() -> new RuntimeException("Sottomissione non trovata"));
        Valutazione valutazione = sottomissione.aggiornaValutazione(punteggio, commento);
        valutazioneRepository.save(valutazione);
        return valutazione;
    }

    public List<Valutazione> getValutazioniTeam(Long teamId) { return valutazioneRepository.findByTeamId(teamId); }
}
