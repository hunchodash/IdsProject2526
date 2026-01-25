package it.hackhub.repository;

import it.hackhub.domain.Team;

import java.util.Optional;

public interface TeamRepository {
    Optional<Team> findById(Long id);
    void save(Team team);
}
