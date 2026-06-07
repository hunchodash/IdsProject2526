package it.hackhub.repository.memory;

import it.hackhub.domain.InvitoTeam;
import it.hackhub.repository.InvitoTeamRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryInvitoTeamRepository implements InvitoTeamRepository {
    private final Map<Long, InvitoTeam> data = new HashMap<>();

    public void save(InvitoTeam invitoTeam) { data.put(invitoTeam.getId(), invitoTeam); }
    public Optional<InvitoTeam> findById(Long id) { return Optional.ofNullable(data.get(id)); }
    public List<InvitoTeam> findByUtenteInvitatoId(Long utenteId) {
        return data.values().stream()
                .filter(i -> Objects.equals(i.getUtenteInvitatoId(), utenteId))
                .collect(Collectors.toList());
    }
    public List<InvitoTeam> findByTeamId(Long teamId) {
        return data.values().stream()
                .filter(i -> Objects.equals(i.getTeamId(), teamId))
                .collect(Collectors.toList());
    }
    public List<InvitoTeam> findAll() { return new ArrayList<>(data.values()); }
}
