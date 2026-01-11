package it.hackhub.repository.memory;

import it.hackhub.domain.Team;
import it.hackhub.repository.TeamRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryTeamRepository implements TeamRepository {
    private final Map<Long, Team> data = new HashMap<>();

    @Override
    public Optional<Team> findById(Long id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public void save(Team team) {
        data.put(team.getId(), team);
    }
}
