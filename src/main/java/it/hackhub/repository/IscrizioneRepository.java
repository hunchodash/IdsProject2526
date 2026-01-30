package it.hackhub.repository;

import it.hackhub.domain.Iscrizione;
import java.util.List;

public interface IscrizioneRepository {
    boolean existsByHackathonIdAndTeamId(Long hackathonId, Long teamId);
    void save(Iscrizione iscrizione);
    List<Iscrizione> findByTeamId(Long teamId);
    List<Iscrizione> findByHackathonId(Long hackathonId);
}