package it.hackhub.controller;

import it.hackhub.domain.ClassificaEntry;
import it.hackhub.domain.Hackathon;
import it.hackhub.domain.Team;
import it.hackhub.domain.staff.Giudice;
import it.hackhub.domain.staff.Mentore;
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

    public Hackathon creaHackathon(String nome,
                                   String regolamento,
                                   LocalDateTime scadenzaIscrizioni,
                                   LocalDateTime dataInizio,
                                   LocalDateTime dataFine,
                                   String luogo,
                                   double premioInDenaro,
                                   int maxTeamSize,
                                   Organizzatore organizzatore,
                                   Giudice giudice,
                                   List<Mentore> mentori) {
        return hackathonService.creaHackathon(
                nome,
                regolamento,
                scadenzaIscrizioni,
                dataInizio,
                dataFine,
                luogo,
                premioInDenaro,
                maxTeamSize,
                organizzatore,
                giudice,
                mentori
        );
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

    public List<Hackathon> consultaHackathonAssegnati(Long staffId) {
        return hackathonService.consultaHackathonAssegnati(staffId);
    }

    public List<Hackathon> consultaHackathonInValutazione(Long giudiceId) {
        return hackathonService.consultaHackathonInValutazione(giudiceId);
    }

    public Hackathon recuperaDettagliHackathon(Long hackathonId) {
        return hackathonService.getHackathon(hackathonId);
    }

    public List<Hackathon> consultaHackathon() {
        return hackathonService.consultaHackathon();
    }
}