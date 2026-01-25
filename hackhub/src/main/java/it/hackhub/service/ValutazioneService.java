package it.hackhub.service;

import it.hackhub.domain.*;
import it.hackhub.domain.state.Concluso;
import it.hackhub.domain.strategy.StrategiaVittoria;
import it.hackhub.adapter.PaymentAdapter;
import it.hackhub.repository.*;
import it.hackhub.service.exception.*;
import java.util.List;
import org.springframework.stereotype.Service;

@Service

public class ValutazioneService {
    private final ValutazioneRepository valutazioneRepo;
    private final HackathonRepository hackathonRepo;
    private final PaymentAdapter paymentAdapter;

    public ValutazioneService(ValutazioneRepository valutazioneRepo,
                              HackathonRepository hackathonRepo,
                              PaymentAdapter paymentAdapter) {
        this.valutazioneRepo = valutazioneRepo;
        this.hackathonRepo = hackathonRepo;
        this.paymentAdapter = paymentAdapter;
    }

    public void valutaTeam(Long hackathonId, Long teamId, int punteggio, String commento) {
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(HackathonNonTrovatoException::new);

        if (!hackathon.getStato().puoValutare()) {
            throw new RuntimeException("Errore: L'hackathon non è in fase di valutazione.");
        }

        Valutazione valutazione = new Valutazione(hackathonId, teamId, punteggio, commento);
        valutazioneRepo.save(valutazione);
    }

    public Long proclamaVincitore(Long hackathonId, StrategiaVittoria strategia) {
// Sostituisci la riga dell'hackathon con questa:
        Hackathon hackathon = hackathonRepo.findById(hackathonId)
                .orElseThrow(() -> new RuntimeException("Hackathon non trovato"));        List<Valutazione> valutazioni = valutazioneRepo.findByHackathonId(hackathonId);
        Long vincitoreId = strategia.calcolaVincitore(valutazioni);

        if (vincitoreId != null) {
            paymentAdapter.erogaPremio(vincitoreId, 1000.0);
            hackathon.setStato(new Concluso());
        }
        return vincitoreId;
    }
}