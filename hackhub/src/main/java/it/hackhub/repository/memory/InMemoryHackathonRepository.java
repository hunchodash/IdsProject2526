package it.hackhub.repository.memory;

import it.hackhub.domain.Hackathon;
import it.hackhub.repository.HackathonRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

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
}
