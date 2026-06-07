package it.hackhub.repository.memory;

import it.hackhub.domain.Team;
import it.hackhub.domain.Utente;
import it.hackhub.repository.TeamRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryTeamRepository implements TeamRepository {
    private final Map<Long, Team> data = new HashMap<>();

    public void save(Team team) { data.put(team.getId(), team); }
    public Optional<Team> findById(Long id) { return Optional.ofNullable(data.get(id)); }
    public Optional<Team> findByNomeTeam(String nomeTeam) {
        return data.values().stream().filter(t -> t.getNomeTeam().equalsIgnoreCase(nomeTeam)).findFirst();
    }
    public List<Team> findByMembro(Utente utente) {
        return data.values().stream().filter(t -> t.getMembri().contains(utente)).collect(Collectors.toList());
    }
    public List<Team> findAll() { return new ArrayList<>(data.values()); }
}
