package it.hackhub.controller;

import it.hackhub.domain.Utente;
import it.hackhub.service.UtenteService;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class AutenticazioneController {
    private final UtenteService utenteService;

    public AutenticazioneController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    public Optional<Utente> accedi(String email, String password) {
        return utenteService.accedi(email, password);
    }

    public Utente registra(String nome, String email, String password) {
        return utenteService.registraUtente(nome, email, password);
    }
}
