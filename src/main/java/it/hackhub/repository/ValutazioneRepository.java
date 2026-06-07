package it.hackhub.repository;

import it.hackhub.domain.Valutazione;
import java.util.List;

public interface ValutazioneRepository {
    void save(Valutazione valutazione);
    List<Valutazione> findByTeamId(Long teamId);
    List<Valutazione> findByHackathonId(Long hackathonId);
    List<Valutazione> findBySottomissioneId(Long sottomissioneId);
    List<Valutazione> findAll();
}
