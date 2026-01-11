package it.hackhub.repository.memory;

import it.hackhub.domain.Iscrizione;
import it.hackhub.repository.IscrizioneRepository;

import java.util.HashSet;
import java.util.Set;

public class InMemoryIscrizioneRepository implements IscrizioneRepository {
    private final Set<String> iscrizioni = new HashSet<>();

    @Override
    public boolean existsByHackathonIdAndTeamId(Long hackathonId, Long teamId) {
        return iscrizioni.contains(key(hackathonId, teamId));
    }

    @Override
    public void save(Iscrizione iscrizione) {
        iscrizioni.add(key(iscrizione.getHackathonId(), iscrizione.getTeamId()));
    }

    private String key(Long hackathonId, Long teamId) {
        return hackathonId + ":" + teamId;
    }
}
