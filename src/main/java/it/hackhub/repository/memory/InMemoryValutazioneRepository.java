package it.hackhub.repository.memory;

import it.hackhub.domain.Valutazione;
import it.hackhub.repository.ValutazioneRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryValutazioneRepository implements ValutazioneRepository {
    private final List<Valutazione> databaseValutazioni = new ArrayList<>();

    @Override
    public void save(Valutazione v) {
        databaseValutazioni.add(v);
    }

    @Override
    public List<Valutazione> findByHackathonId(Long hackathonId) {
        return databaseValutazioni.stream()
                .filter(v -> v.getHackathonId().equals(hackathonId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Valutazione> findByTeamId(Long teamId) {
        return databaseValutazioni.stream()
                .filter(v -> v.getTeamId().equals(teamId))
                .collect(Collectors.toList());
    }
}