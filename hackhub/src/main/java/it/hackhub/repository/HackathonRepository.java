package it.hackhub.repository;

import it.hackhub.domain.Hackathon;

import java.util.Optional;

public interface HackathonRepository {
    Optional<Hackathon> findById(Long id);
    void save(Hackathon hackathon);
}
