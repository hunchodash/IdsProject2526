package it.hackhub.cli;

import it.hackhub.domain.Hackathon;
import it.hackhub.domain.Sottomissione;
import it.hackhub.domain.Team;
import it.hackhub.domain.Utente;
import it.hackhub.domain.staff.Giudice;
import it.hackhub.domain.staff.Mentore;
import it.hackhub.domain.staff.Organizzatore;
import it.hackhub.repository.HackathonRepository;
import it.hackhub.repository.SottomissioneRepository;
import it.hackhub.repository.TeamRepository;
import it.hackhub.repository.UtenteRepository;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class DatiDimostrativiRunner {
    private final UtenteRepository utenteRepository;
    private final TeamRepository teamRepository;
    private final HackathonRepository hackathonRepository;
    private final SottomissioneRepository sottomissioneRepository;

    public DatiDimostrativiRunner(UtenteRepository utenteRepository,
                                  TeamRepository teamRepository,
                                  HackathonRepository hackathonRepository,
                                  SottomissioneRepository sottomissioneRepository) {
        this.utenteRepository = utenteRepository;
        this.teamRepository = teamRepository;
        this.hackathonRepository = hackathonRepository;
        this.sottomissioneRepository = sottomissioneRepository;
    }

    public void carica() {
        if (!utenteRepository.findAll().isEmpty()) {
            return;
        }

        Utente utente = new Utente(1L, "Mario Rossi", "mario@hackhub.it", "password");
        Organizzatore organizzatore = new Organizzatore(2L, "Admin", "admin@hackhub.it", "password");
        Giudice giudice = new Giudice(3L, "Giudice", "giudice@hackhub.it", "password");
        Mentore mentore = new Mentore(4L, "Mentore", "mentore@hackhub.it", "password");

        Team team = new Team(1L, "Team Alpha");
        team.aggiungiMembro(utente);

        Hackathon hackathon = new Hackathon(
                1L,
                "HackHub Demo",
                "Regolamento base",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(2),
                4,
                organizzatore,
                giudice
        );
        hackathon.aggiungiMentore(mentore);
        hackathon.iscriviTeam(team);

        Sottomissione sottomissione = new Sottomissione(1L, hackathon.getId(), team.getId(), "https://repo.demo/team-alpha");
        hackathon.associaSottomissione(sottomissione);

        utenteRepository.save(utente);
        utenteRepository.save(organizzatore);
        utenteRepository.save(giudice);
        utenteRepository.save(mentore);
        teamRepository.save(team);
        hackathonRepository.save(hackathon);
        sottomissioneRepository.save(sottomissione);

        System.out.println("Dati dimostrativi caricati.");
    }
}
