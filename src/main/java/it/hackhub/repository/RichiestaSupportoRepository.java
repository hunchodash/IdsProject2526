package it.hackhub.repository;

import it.hackhub.domain.RichiestaSupporto;
import java.util.List;
import java.util.Optional;

public interface RichiestaSupportoRepository {
    void save(RichiestaSupporto richiesta);
    List<RichiestaSupporto> findByMentoreIdAndGestita(Long mentoreId, boolean gestita);
    Optional<RichiestaSupporto> findById(Long id);
}