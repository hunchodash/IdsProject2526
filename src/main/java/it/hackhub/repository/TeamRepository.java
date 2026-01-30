package it.hackhub.repository;

import it.hackhub.domain.Team;
import it.hackhub.domain.Utente;
import java.util.List;
import java.util.Optional;

public interface TeamRepository {
    Optional<Team> findById(Long id);
    void save(Team team);
    List<Team> findByMembro(Utente utente);
}