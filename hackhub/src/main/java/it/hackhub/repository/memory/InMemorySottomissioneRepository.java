package it.hackhub.repository.memory;
import it.hackhub.domain.Sottomissione;
import it.hackhub.repository.SottomissioneRepository;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository

public class InMemorySottomissioneRepository implements SottomissioneRepository {
    private Map<String, Sottomissione> sottomissioni = new HashMap<>();

    @Override
    public void save(Sottomissione s) {
        sottomissioni.put(s.getHackathonId() + "-" + s.getTeamId(), s);
    }

    @Override
    public Optional<Sottomissione> findByHackathonIdAndTeamId(Long hId, Long tId) {
        return Optional.ofNullable(sottomissioni.get(hId + "-" + tId));
    }
}