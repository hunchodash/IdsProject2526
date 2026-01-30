package it.hackhub.repository.memory;

import it.hackhub.domain.Hackathon;
import it.hackhub.domain.state.StatoHackathon;
import it.hackhub.repository.HackathonRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryHackathonRepository implements HackathonRepository {
    private final Map<Long, Hackathon> data = new HashMap<>();

    @Override
    public Optional<Hackathon> findById(Long id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public void save(Hackathon hackathon) {
        data.put(hackathon.getId(), hackathon);
    }

    @Override
    public List<Hackathon> findAll() {  // ← AGGIUNGI
        return new ArrayList<>(data.values());
    }

    @Override
    public List<Hackathon> findByStato(StatoHackathon stato) {  // ← AGGIUNGI
        return data.values().stream()
                .filter(h -> h.getStato().getClass().equals(stato.getClass()))
                .collect(Collectors.toList());
    }
}