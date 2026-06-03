package it.hackhub.service;

import it.hackhub.domain.RichiestaSupporto;
import it.hackhub.repository.RichiestaSupportoRepository;
import org.springframework.stereotype.Service;

@Service
public class SupportoService {
    private final RichiestaSupportoRepository richiestaSupportoRepository;

    public SupportoService(RichiestaSupportoRepository richiestaSupportoRepository) {
        this.richiestaSupportoRepository = richiestaSupportoRepository;
    }

    public RichiestaSupporto richiediSupporto(Long utenteId, String topic, String descrizione) {
        RichiestaSupporto richiesta = new RichiestaSupporto(System.currentTimeMillis(), utenteId, topic, descrizione);
        richiesta.salvaNelSistema();
        richiestaSupportoRepository.save(richiesta);
        return richiesta;
    }
}
