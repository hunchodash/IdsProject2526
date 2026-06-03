package it.hackhub.controller;

import it.hackhub.domain.Hackathon;
import it.hackhub.domain.staff.Giudice;
import it.hackhub.domain.staff.Organizzatore;
import it.hackhub.service.HackathonService;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class HackathonController {
    private final HackathonService hackathonService;

    public HackathonController(HackathonService hackathonService) {
        this.hackathonService = hackathonService;
    }

    public Hackathon creaHackathon(String nome, String regolamento,
                                   LocalDateTime scadenzaIscrizioni,
                                   LocalDateTime dataInizio,
                                   LocalDateTime dataFine,
                                   int maxTeamSize,
                                   Organizzatore organizzatore,
                                   Giudice giudice) {
        return hackathonService.creaHackathon(nome, regolamento, scadenzaIscrizioni,
                dataInizio, dataFine, maxTeamSize, organizzatore, giudice);
    }

    public List<Hackathon> consultaHackathon() { return hackathonService.consultaHackathon(); }
}
