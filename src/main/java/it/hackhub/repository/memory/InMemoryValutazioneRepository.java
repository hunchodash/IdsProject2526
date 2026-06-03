package it.hackhub.repository.memory;

import it.hackhub.domain.Valutazione;
import it.hackhub.repository.ValutazioneRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryValutazioneRepository implements ValutazioneRepository {
    private final List<Valutazione> data = new ArrayList<>();

    public void save(Valutazione valutazione) { data.add(valutazione); }
    public List<Valutazione> findByTeamId(Long teamId) {
        return data.stream().filter(v -> v.getTeamId().equals(teamId)).collect(Collectors.toList());
    }
}
