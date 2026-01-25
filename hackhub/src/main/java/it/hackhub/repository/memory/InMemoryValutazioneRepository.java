package it.hackhub.repository.memory;

import it.hackhub.domain.Valutazione;
import it.hackhub.repository.ValutazioneRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

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
}