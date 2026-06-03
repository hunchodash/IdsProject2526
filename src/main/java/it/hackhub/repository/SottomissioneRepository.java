package it.hackhub.repository;

import it.hackhub.domain.Sottomissione;
import java.util.List;
import java.util.Optional;

public interface SottomissioneRepository {
    void save(Sottomissione sottomissione);
    Optional<Sottomissione> findById(Long id);
    Optional<Sottomissione> findByHackathonIdAndTeamId(Long hackathonId, Long teamId);
    List<Sottomissione> findAll();
}
