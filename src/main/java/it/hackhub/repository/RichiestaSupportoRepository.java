package it.hackhub.repository;

import it.hackhub.domain.RichiestaSupporto;
import java.util.List;

public interface RichiestaSupportoRepository {
    void save(RichiestaSupporto richiestaSupporto);
    List<RichiestaSupporto> findAll();
}
