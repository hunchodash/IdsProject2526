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

    public void registraUtente(String nome, String email, String password) {
        Long nuovoId = System.currentTimeMillis();
        Utente nuovoUtente = new Utente(nuovoId, nome, email, password);
        utenteRepository.save(nuovoUtente);
    }

    public Optional<Utente> login(String email, String password) {
        Optional<Utente> utente = utenteRepository.findByEmail(email);
        if (utente.isPresent() && utente.get().getPassword().equals(password)) {
            return utente;
        }
        return Optional.empty();
    }

    public Optional<Utente> findByEmail(String email) {
        return utenteRepository.findByEmail(email);
    }

    public Optional<Utente> findMentoreDisponibile() {
        return utenteRepository.findMentore();
    }

    public void salvaUtente(Utente utente) {
        utenteRepository.save(utente);
    }
}