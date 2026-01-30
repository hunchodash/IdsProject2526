package it.hackhub.repository;

import it.hackhub.domain.Hackathon;
import it.hackhub.domain.state.StatoHackathon;
import java.util.List;
import java.util.Optional;

public interface HackathonRepository {
    Optional<Hackathon> findById(Long id);
    void save(Hackathon hackathon);
    List<Hackathon> findAll();
    List<Hackathon> findByStato(StatoHackathon stato);
}