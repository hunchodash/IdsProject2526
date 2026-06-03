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
        if (nome == null || nome.isBlank() || email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("Nome, email e password sono obbligatori");
        }
        Utente utente = new Utente(System.currentTimeMillis(), nome, email, password);
        utenteRepository.save(utente);
        return utente;
    }

    public Optional<Utente> accedi(String email, String password) {
        return utenteRepository.findByEmail(email)
                .filter(utente -> utente.getPassword().equals(password));
    }
}
