package it.hackhub.repository;
import it.hackhub.domain.Valutazione;
import java.util.List;

public interface ValutazioneRepository {
    void save(Valutazione valutazione); // <--- Deve esserci questa riga!
    List<Valutazione> findByHackathonId(Long hackathonId);
}