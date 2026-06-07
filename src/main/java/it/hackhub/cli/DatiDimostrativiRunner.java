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

        Hackathon hackathonIscrizioni = new Hackathon(
                1L,
                "HackHub Demo - Iscrizioni",
                "Regolamento base",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(3),
                "Online",
                500.0,
                4,
                organizzatore
        );
        hackathonIscrizioni.aggiungiGiudice(giudice);
        hackathonIscrizioni.aggiungiMentore(mentore);
        hackathonIscrizioni.iscriviTeam(team);

        Hackathon hackathonInCorso = new Hackathon(
                2L,
                "HackHub Demo - In Corso",
                "Regolamento base",
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(2),
                "Online",
                750.0,
                4,
                organizzatore
        );
        hackathonInCorso.aggiungiGiudice(giudice);
        hackathonInCorso.aggiungiMentore(mentore);
        hackathonInCorso.registraIscrizionePreesistente(team);

        Hackathon hackathonValutazione = new Hackathon(
                3L,
                "HackHub Demo - Valutazione",
                "Regolamento base",
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(4),
                LocalDateTime.now().minusDays(1),
                "Online",
                1000.0,
                4,
                organizzatore
        );
        hackathonValutazione.aggiungiGiudice(giudice);
        hackathonValutazione.aggiungiMentore(mentore);
        hackathonValutazione.registraIscrizionePreesistente(team);

        Sottomissione sottomissione = new Sottomissione(
                1L,
                hackathonValutazione.getId(),
                team.getId(),
                "https://repo.demo/team-alpha"
        );
        hackathonValutazione.associaSottomissione(sottomissione);

        utenteRepository.save(utente);
        utenteRepository.save(organizzatore);
        utenteRepository.save(giudice);
        utenteRepository.save(mentore);
        teamRepository.save(team);
        hackathonRepository.save(hackathonIscrizioni);
        hackathonRepository.save(hackathonInCorso);
        hackathonRepository.save(hackathonValutazione);
        sottomissioneRepository.save(sottomissione);

        System.out.println("Dati dimostrativi caricati.");
    }
}