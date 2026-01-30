package it.hackhub.repository.memory;

import it.hackhub.domain.Iscrizione;
import it.hackhub.repository.IscrizioneRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryIscrizioneRepository implements IscrizioneRepository {
    private final Set<String> iscrizioni = new HashSet<>();
    private final List<Iscrizione> iscrizioniList = new ArrayList<>();  // ← AGGIUNGI

    @Override
    public boolean existsByHackathonIdAndTeamId(Long hackathonId, Long teamId) {
        return iscrizioni.contains(key(hackathonId, teamId));
    }

    @Override
    public void save(Iscrizione iscrizione) {
        iscrizioni.add(key(iscrizione.getHackathonId(), iscrizione.getTeamId()));
        iscrizioniList.add(iscrizione);  // ← AGGIUNGI
    }

    @Override
    public List<Iscrizione> findByTeamId(Long teamId) {  // ← AGGIUNGI
        return iscrizioniList.stream()
                .filter(i -> i.getTeamId().equals(teamId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Iscrizione> findByHackathonId(Long hackathonId) {  // ← AGGIUNGI
        return iscrizioniList.stream()
                .filter(i -> i.getHackathonId().equals(hackathonId))
                .collect(Collectors.toList());
    }

    private String key(Long hackathonId, Long teamId) {
        return hackathonId + ":" + teamId;
    }
}