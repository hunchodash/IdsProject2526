package it.hackhub.repository.memory;

import it.hackhub.domain.Hackathon;
import it.hackhub.repository.HackathonRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class InMemoryHackathonRepository implements HackathonRepository {
    private final Map<Long, Hackathon> data = new HashMap<>();

    public void save(Hackathon hackathon) { data.put(hackathon.getId(), hackathon); }
    public Optional<Hackathon> findById(Long id) { return Optional.ofNullable(data.get(id)); }
    public List<Hackathon> findAll() { return new ArrayList<>(data.values()); }
}
