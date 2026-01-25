package it.hackhub;

import it.hackhub.domain.*;
import it.hackhub.domain.state.*;
import it.hackhub.domain.staff.*;
import it.hackhub.domain.strategy.MigliorPunteggioStrategy;
import it.hackhub.service.*;
import it.hackhub.repository.*; // Assicurati di avere le interfacce repository qui
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DemoRunner implements CommandLineRunner {


    @Autowired private TeamService teamService;
    @Autowired private SubmissionService subService;
    @Autowired private ValutazioneService valService;
    @Autowired private MentoreService mentoreService;

    @Autowired private HackathonRepository hackathonRepo;
    @Autowired private TeamRepository teamRepo;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> HACKHUB STARTING ON JAVA 23 & SPRING BOOT 3.4.1 <<<");

        // 1. Creazione Staff
        Organizzatore azizOrg = new Organizzatore(1L, "Aziz", "aziz@hackhub.it", "adminPass");
        Giudice giudiceRossi = new Giudice(2L, "Rossi", "rossi@univ.it", "judgePass");
        Mentore mentorePiero = new Mentore(3L, "Piero", "piero@mentors.it", "mentorPass");

        // 2. Creazione Hackathon
        Hackathon hackathon = new Hackathon(1L, "Java 23 Challenge", 4, new IscrizioneAperta(), azizOrg, giudiceRossi);
        hackathon.aggiungiMentore(mentorePiero);
        hackathonRepo.save(hackathon);

        // 3. Iscrizione Team
        Team team = new Team(10L, "Dev Team");
        team.aggiungiMembro(new Utente(101L, "Mario", "m@m.it", "pw"));
        teamRepo.save(team);

        // 4. Logica di business
        teamService.iscriviTeam(10L, 1L);
        System.out.println("LOG: Team iscritto correttamente.");

        hackathon.setStato(new InCorso());
        String link = mentoreService.proponiCall(mentorePiero, 10L);
        System.out.println("LOG: Call generata: " + link);

        subService.inviaSottomissione(1L, 10L, "github.com/project");

        hackathon.setStato(new InValutazione());
        valService.valutaTeam(1L, 10L, 10, "Eccellente");

        Long vincitore = valService.proclamaVincitore(1L, new MigliorPunteggioStrategy());
        System.out.println(">>> VINCITORE ID: " + vincitore);
    }
}