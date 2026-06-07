package it.hackhub.service;

import it.hackhub.domain.Utente;
import it.hackhub.repository.UtenteRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UtenteService {
    private final UtenteRepository utenteRepository;

    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    public Utente registraUtente(String nome, String email, String password) {
        if (utenteRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Email già registrata");
        }
        Utente utente = new Utente(System.currentTimeMillis(), nome, email, password);
        utenteRepository.save(utente);
        return utente;
    }

    public Optional<Utente> accedi(String email, String password) {
        return utenteRepository.findByEmail(email)
                .filter(utente -> utente.getPassword().equals(password));
    }

    public Utente recuperaUtente(Long utenteId) {
        return utenteRepository.findById(utenteId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    public Utente aggiornaProfilo(Long utenteId, String nuovoNome, String nuovaEmail) {
        Utente utente = recuperaUtente(utenteId);
        utenteRepository.findByEmail(nuovaEmail)
                .filter(u -> !u.getId().equals(utenteId))
                .ifPresent(u -> { throw new IllegalStateException("Email già registrata"); });
        utente.aggiornaProfilo(nuovoNome, nuovaEmail);
        utenteRepository.save(utente);
        return utente;
    }
}
