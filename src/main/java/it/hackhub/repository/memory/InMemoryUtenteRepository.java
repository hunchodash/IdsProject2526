package it.hackhub.repository.memory;

import it.hackhub.domain.Utente;
import it.hackhub.repository.UtenteRepository;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryUtenteRepository implements UtenteRepository {
    private final Map<Long, Utente> data = new HashMap<>();
    private final Map<String, Utente> byEmail = new HashMap<>();

    @Override
    public List<Utente> findAll() {
        return new ArrayList<>(data.values());
    }

    public void save(Utente utente) {
        data.put(utente.getId(), utente);
        byEmail.put(utente.getEmail(), utente);
    }
    public Optional<Utente> findById(Long id) { return Optional.ofNullable(data.get(id)); }
    public Optional<Utente> findByEmail(String email) { return Optional.ofNullable(byEmail.get(email)); }
}
