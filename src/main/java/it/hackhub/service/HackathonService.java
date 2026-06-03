package it.hackhub.service;

import it.hackhub.domain.Hackathon;
import it.hackhub.domain.staff.Giudice;
import it.hackhub.domain.staff.Mentore;
import it.hackhub.domain.staff.Organizzatore;
import it.hackhub.repository.HackathonRepository;
import it.hackhub.service.exception.HackathonNonTrovatoException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HackathonService {
    private final HackathonRepository hackathonRepository;

    public HackathonService(HackathonRepository hackathonRepository) {
        this.hackathonRepository = hackathonRepository;
    }

    public Hackathon creaHackathon(String nome, String regolamento,
                                   LocalDateTime scadenzaIscrizioni,
                                   LocalDateTime dataInizio,
                                   LocalDateTime dataFine,
                                   int maxTeamSize,
                                   Organizzatore organizzatore,
                                   Giudice giudice) {
        if (nome == null || nome.isBlank() || maxTeamSize <= 0) {
            throw new IllegalArgumentException("Dati hackathon non validi");
        }
        Hackathon hackathon = new Hackathon(System.currentTimeMillis(), nome, regolamento,
                scadenzaIscrizioni, dataInizio, dataFine, maxTeamSize, organizzatore, giudice);
        hackathon.salvaNelSistema();
        hackathonRepository.save(hackathon);
        return hackathon;
    }

    public void aggiungiMentore(Long hackathonId, Mentore mentore) {
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow(HackathonNonTrovatoException::new);
        hackathon.aggiungiMentore(mentore);
        hackathonRepository.save(hackathon);
    }

    public List<Hackathon> consultaHackathon() { return hackathonRepository.findAll(); }
}
