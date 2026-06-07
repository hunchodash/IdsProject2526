package it.hackhub.repository;

import it.hackhub.domain.InvitoTeam;
import java.util.List;
import java.util.Optional;

public interface InvitoTeamRepository {
    void save(InvitoTeam invitoTeam);
    Optional<InvitoTeam> findById(Long id);
    List<InvitoTeam> findByUtenteInvitatoId(Long utenteId);
    List<InvitoTeam> findByTeamId(Long teamId);
    List<InvitoTeam> findAll();
}
