package it.hackhub.repository.memory;

import it.hackhub.domain.RichiestaSupporto;
import it.hackhub.repository.RichiestaSupportoRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public class InMemoryRichiestaSupportoRepository implements RichiestaSupportoRepository {
    private final Map<Long, RichiestaSupporto> data = new HashMap<>();

    public void save(RichiestaSupporto richiestaSupporto) { data.put(richiestaSupporto.getId(), richiestaSupporto); }
    public List<RichiestaSupporto> findAll() { return new ArrayList<>(data.values()); }
}
