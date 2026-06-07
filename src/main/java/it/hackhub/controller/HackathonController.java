package it.hackhub.controller;

import it.hackhub.domain.ClassificaEntry;
import it.hackhub.domain.Hackathon;
import it.hackhub.domain.Team;
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

    public Hackathon creaHackathon(String nome, String regolamento,
                                   LocalDateTime scadenzaIscrizioni,
                                   LocalDateTime dataInizio,
                                   LocalDateTime dataFine,
                                   int maxTeamSize,
                                   Organizzatore organizzatore) {
        return hackathonService.creaHackathon(nome, regolamento, scadenzaIscrizioni,
                dataInizio, dataFine, maxTeamSize, organizzatore);
    }

    public void assegnaStaff(Long hackathonId, String email, String ruolo) {
        hackathonService.assegnaStaff(hackathonId, email, ruolo);
    }

    public Team proclamaVincitore(Long hackathonId, Long teamVincitoreId) {
        return hackathonService.proclamaVincitore(hackathonId, teamVincitoreId);
    }

    public Team proclamaVincitoreAutomaticamente(Long hackathonId) {
        return hackathonService.proclamaVincitoreAutomaticamente(hackathonId);
    }

    public List<ClassificaEntry> calcolaClassifica(Long hackathonId) {
        return hackathonService.calcolaClassifica(hackathonId);
    }

    public Hackathon recuperaDettagliHackathon(Long hackathonId) {
        return hackathonService.getHackathon(hackathonId);
    }

    public List<Hackathon> consultaHackathon() { return hackathonService.consultaHackathon(); }
}

