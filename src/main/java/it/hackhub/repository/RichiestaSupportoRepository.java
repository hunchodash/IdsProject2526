package it.hackhub.repository;

import it.hackhub.domain.RichiestaSupporto;
import java.util.List;
import java.util.Optional;

public interface RichiestaSupportoRepository {
    void save(RichiestaSupporto richiestaSupporto);
    Optional<RichiestaSupporto> findById(Long id);
    List<RichiestaSupporto> findAll();
    List<RichiestaSupporto> findByHackathonId(Long hackathonId);
    List<RichiestaSupporto> findAperte();
}
