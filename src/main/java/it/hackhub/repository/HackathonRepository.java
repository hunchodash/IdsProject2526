package it.hackhub.repository;

import it.hackhub.domain.Hackathon;
import java.util.List;
import java.util.Optional;

public interface HackathonRepository {
    void save(Hackathon hackathon);
    Optional<Hackathon> findById(Long id);
    List<Hackathon> findAll();
}
