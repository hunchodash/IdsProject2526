package it.hackhub.repository;

import it.hackhub.domain.Team;
import it.hackhub.domain.Utente;
import java.util.List;
import java.util.Optional;

public interface TeamRepository {
    void save(Team team);
    Optional<Team> findById(Long id);
    Optional<Team> findByNomeTeam(String nomeTeam);
    List<Team> findByMembro(Utente utente);
    List<Team> findAll();
}
