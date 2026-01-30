package it.hackhub.repository.memory;

import it.hackhub.domain.Utente;
import it.hackhub.domain.staff.Mentore;
import it.hackhub.repository.UtenteRepository;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryUtenteRepository implements UtenteRepository {
    private final Map<Long, Utente> data = new HashMap<>();
    private final Map<String, Utente> byEmail = new HashMap<>();

    @Override
    public Optional<Utente> findById(Long id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public Optional<Utente> findByEmail(String email) {
        return Optional.ofNullable(byEmail.get(email));
    }

    @Override
    public void save(Utente utente) {
        data.put(utente.getId(), utente);
        byEmail.put(utente.getEmail(), utente);
    }

    @Override
    public Optional<Utente> findMentore() {
        return data.values().stream()
                .filter(u -> u instanceof Mentore)
                .findFirst();
    }
}