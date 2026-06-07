package it.hackhub.repository;

import it.hackhub.domain.Utente;
import java.util.List;
import java.util.Optional;

public interface UtenteRepository {
    void save(Utente utente);
    Optional<Utente> findById(Long id);
    Optional<Utente> findByEmail(String email);
    List<Utente> findAll();
}