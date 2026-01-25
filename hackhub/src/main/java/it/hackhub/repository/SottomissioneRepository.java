package it.hackhub.repository;

import it.hackhub.domain.Sottomissione;
import java.util.Optional;

public interface SottomissioneRepository {
    void save(Sottomissione sottomissione);
    Optional<Sottomissione> findByHackathonIdAndTeamId(Long hackathonId, Long teamId);
}