package it.hackhub.repository.memory;

import it.hackhub.domain.Sottomissione;
import it.hackhub.repository.SottomissioneRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemorySottomissioneRepository implements SottomissioneRepository {
    private final Map<Long, Sottomissione> data = new HashMap<>();

    public void save(Sottomissione sottomissione) { data.put(sottomissione.getId(), sottomissione); }
    public Optional<Sottomissione> findById(Long id) { return Optional.ofNullable(data.get(id)); }
    public Optional<Sottomissione> findByHackathonIdAndTeamId(Long hackathonId, Long teamId) {
        return data.values().stream()
                .filter(s -> s.getHackathonId().equals(hackathonId) && s.getTeamId().equals(teamId))
                .findFirst();
    }
    public List<Sottomissione> findByHackathonId(Long hackathonId) {
        return data.values().stream().filter(s -> s.getHackathonId().equals(hackathonId)).collect(Collectors.toList());
    }
    public List<Sottomissione> findAll() { return new ArrayList<>(data.values()); }
}
