package it.hackhub.repository.memory;

import it.hackhub.domain.RichiestaSupporto;
import it.hackhub.repository.RichiestaSupportoRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryRichiestaSupportoRepository implements RichiestaSupportoRepository {
    private final Map<Long, RichiestaSupporto> data = new HashMap<>();

    public void save(RichiestaSupporto richiestaSupporto) { data.put(richiestaSupporto.getId(), richiestaSupporto); }
    public Optional<RichiestaSupporto> findById(Long id) { return Optional.ofNullable(data.get(id)); }
    public List<RichiestaSupporto> findAll() { return new ArrayList<>(data.values()); }
    public List<RichiestaSupporto> findByHackathonId(Long hackathonId) {
        return data.values().stream()
                .filter(r -> Objects.equals(r.getHackathonId(), hackathonId))
                .collect(Collectors.toList());
    }
    public List<RichiestaSupporto> findAperte() {
        return data.values().stream().filter(RichiestaSupporto::isAperta).collect(Collectors.toList());
    }
}
