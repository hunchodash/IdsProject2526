package it.hackhub.repository;

import it.hackhub.domain.Iscrizione;

public interface IscrizioneRepository {
    boolean existsByHackathonIdAndTeamId(Long hackathonId, Long teamId);
    void save(Iscrizione iscrizione);
}
