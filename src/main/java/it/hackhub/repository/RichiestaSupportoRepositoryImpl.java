package it.hackhub.repository;

import it.hackhub.domain.RichiestaSupporto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository // Fondamentale! Dice a Spring di iniettare questa classe nel MentoreService
public class RichiestaSupportoRepositoryImpl implements RichiestaSupportoRepository {

    // Lista in memoria che funge da database
    private final List<RichiestaSupporto> databaseRichieste = new ArrayList<>();

    @Override
    public void save(RichiestaSupporto richiesta) {
        // Rimuove la richiesta se esiste già (per l'aggiornamento) e poi la salva
        databaseRichieste.removeIf(r -> r.getId().equals(richiesta.getId()));
        databaseRichieste.add(richiesta);
    }

    @Override
    public List<RichiestaSupporto> findByMentoreIdAndGestita(Long mentoreId, boolean gestita) {
        return databaseRichieste.stream()
                .filter(r -> r.getMentoreAssegnato().getId().equals(mentoreId) && r.isGestita() == gestita)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RichiestaSupporto> findById(Long id) {
        return databaseRichieste.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }
}