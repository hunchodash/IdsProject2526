package it.hackhub.service;

import it.hackhub.domain.Hackathon;
import it.hackhub.domain.HackathonStatus;
import it.hackhub.domain.Sottomissione;
import it.hackhub.domain.Utente;
import it.hackhub.domain.Valutazione;
import it.hackhub.domain.staff.Giudice;
import it.hackhub.repository.HackathonRepository;
import it.hackhub.repository.SottomissioneRepository;
import it.hackhub.repository.UtenteRepository;
import it.hackhub.repository.ValutazioneRepository;
import it.hackhub.service.exception.HackathonNonTrovatoException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ValutazioneService {
    private final SottomissioneRepository sottomissioneRepository;
    private final ValutazioneRepository valutazioneRepository;
    private final HackathonRepository hackathonRepository;
    private final UtenteRepository utenteRepository;

    public ValutazioneService(SottomissioneRepository sottomissioneRepository,
                              ValutazioneRepository valutazioneRepository,
                              HackathonRepository hackathonRepository,
                              UtenteRepository utenteRepository) {
        this.sottomissioneRepository = sottomissioneRepository;
        this.valutazioneRepository = valutazioneRepository;
        this.hackathonRepository = hackathonRepository;
        this.utenteRepository = utenteRepository;
    }

    public Valutazione valutaSottomissione(Long sottomissioneId, int punteggio, String commento) {
        throw new IllegalArgumentException("Per valutare una sottomissione è necessario indicare il giudice");
    }

    public Valutazione valutaSottomissione(Long sottomissioneId, Long giudiceId, int punteggio, String commento) {
        if (giudiceId == null) {
            throw new IllegalArgumentException("Giudice obbligatorio");
        }

        Sottomissione sottomissione = sottomissioneRepository.findById(sottomissioneId)
                .orElseThrow(() -> new RuntimeException("Sottomissione non trovata"));

        Hackathon hackathon = hackathonRepository.findById(sottomissione.getHackathonId())
                .orElseThrow(HackathonNonTrovatoException::new);
        hackathon.aggiornaStatoCorrente();

        Utente utente = utenteRepository.findById(giudiceId)
                .orElseThrow(() -> new IllegalArgumentException("Giudice non trovato"));
        if (!(utente instanceof Giudice)) {
            throw new IllegalArgumentException("L'utente indicato non è un giudice");
        }

        boolean giudiceAssegnato = hackathon.getGiudici().stream()
                .anyMatch(g -> g.getId().equals(giudiceId));
        if (!giudiceAssegnato) {
            throw new IllegalStateException("Il giudice può valutare solo sottomissioni degli hackathon a cui è assegnato");
        }

        if (hackathon.getStato() != HackathonStatus.VALUTAZIONE) {
            throw new IllegalStateException("La sottomissione può essere valutata solo durante la fase di valutazione");
        }

        boolean giaValutataDalGiudice = valutazioneRepository.findBySottomissioneId(sottomissioneId).stream()
                .anyMatch(v -> giudiceId.equals(v.getGiudiceId()));
        if (giaValutataDalGiudice) {
            throw new IllegalStateException("Il giudice ha già valutato questa sottomissione");
        }

        Valutazione valutazione = sottomissione.aggiornaValutazione(giudiceId, punteggio, commento);
        valutazioneRepository.save(valutazione);
        sottomissioneRepository.save(sottomissione);
        return valutazione;
    }

    public List<Valutazione> getValutazioniTeam(Long teamId) { return valutazioneRepository.findByTeamId(teamId); }
    public List<Valutazione> getValutazioniHackathon(Long hackathonId) { return valutazioneRepository.findByHackathonId(hackathonId); }
    public List<Valutazione> getValutazioniSottomissione(Long sottomissioneId) { return valutazioneRepository.findBySottomissioneId(sottomissioneId); }
}
