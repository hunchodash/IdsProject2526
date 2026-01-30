package it.hackhub.repository;

import it.hackhub.domain.Utente;
import java.util.Optional;

public interface UtenteRepository {
    Optional<Utente> findById(Long id);
    Optional<Utente> findByEmail(String email);
    void save(Utente utente);
    Optional<Utente> findMentore();
}