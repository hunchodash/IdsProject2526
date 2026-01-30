package it.hackhub;

import it.hackhub.domain.*;
import it.hackhub.domain.staff.*;
import it.hackhub.domain.state.*;
import it.hackhub.domain.strategy.MigliorPunteggioStrategy;
import it.hackhub.repository.*;
import it.hackhub.service.*;
import it.hackhub.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class HackHubCli implements CommandLineRunner {

    @Autowired private TeamService teamService;
    @Autowired private SubmissionService submissionService;
    @Autowired private ValutazioneService valutazioneService;
    @Autowired private MentoreService mentoreService;

    @Autowired private HackathonRepository hackathonRepository;
    @Autowired private TeamRepository teamRepository;
    @Autowired private UtenteRepository utenteRepository;
    @Autowired private IscrizioneRepository iscrizioneRepository;
    @Autowired private SottomissioneRepository sottomissioneRepository;
    @Autowired private ValutazioneRepository valutazioneRepository;

    private Scanner scanner = new Scanner(System.in);
    private Utente utenteCorrente = null;

    @Override
    public void run(String... args) {
        System.out.println("========================================");
        System.out.println("     BENVENUTO IN HACKHUB v1.0");
        System.out.println("========================================");

        inizializzaDatiTest();

        while (true) {
            if (utenteCorrente == null) {
                menuNonAutenticato();
            } else {
                menuPrincipale();
            }
        }
    }

    private void menuNonAutenticato() {
        System.out.println("\n--- MENU PRINCIPALE ---");
        System.out.println("1. Accedi");
        System.out.println("2. Registrati");
        System.out.println("3. Visualizza hackathon pubblici");
        System.out.println("0. Esci");
        System.out.print("Scegli: ");

        int scelta = leggiIntero();

        switch (scelta) {
            case 1:
                accedi();
                break;
            case 2:
                registrati();
                break;
            case 3:
                visualizzaHackathonPubblici();
                break;
            case 0:
                System.out.println("Grazie per aver usato HackHub. Arrivederci!");
                System.exit(0);
                break;
            default:
                System.out.println("Scelta non valida. Riprova.");
        }
    }

    private void menuPrincipale() {
        System.out.println("\n--- MENU PRINCIPALE ---");
        System.out.println("Benvenuto, " + utenteCorrente.getNome() + "!");
        System.out.println("1. Gestisci team");
        System.out.println("2. Hackathon disponibili");
        System.out.println("3. Le mie iscrizioni");
        System.out.println("4. Invia sottomissione");
        System.out.println("5. Richiedi supporto mentore");
        System.out.println("6. Visualizza valutazioni");
        System.out.println("7. Area Staff (se autorizzato)");
        System.out.println("0. Logout");
        System.out.print("Scegli: ");

        int scelta = leggiIntero();

        switch (scelta) {
            case 1:
                gestisciTeam();
                break;
            case 2:
                hackathonDisponibili();
                break;
            case 3:
                leMieIscrizioni();
                break;
            case 4:
                inviaSottomissione();
                break;
            case 5:
                richiediSupporto();
                break;
            case 6:
                visualizzaValutazioni();
                break;
            case 7:
                areaStaff();
                break;
            case 0:
                utenteCorrente = null;
                System.out.println("Logout effettuato.");
                break;
            default:
                System.out.println("Scelta non valida. Riprova.");
        }
    }

    private void accedi() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();


        Utente utente = utenteRepository.findByEmail(email).orElse(null);

        if (utente != null && utente.getPassword().equals(password)) {
            utenteCorrente = utente;
            System.out.println("✅ Accesso effettuato con successo!");
        } else {
            System.out.println("❌ Email o password errati.");
        }
    }

    private void registrati() {
        System.out.println("--- REGISTRAZIONE NUOVO UTENTE ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Long nuovoId = System.currentTimeMillis(); // Genera ID univoco
        Utente nuovoUtente = new Utente(nuovoId, nome, email, password);
        utenteRepository.save(nuovoUtente);

        System.out.println("Registrazione completata! Ora puoi accedere.");
    }

    private void gestisciTeam() {
        System.out.println("\n--- GESTIONE TEAM ---");


        List<Team> mieiTeam = teamRepository.findByMembro(utenteCorrente);

        if (mieiTeam.isEmpty()) {
            System.out.println("Non fai parte di nessun team.");
            System.out.println("1. Crea nuovo team");
            System.out.println("2. Accetta invito (simulato)");
            System.out.println("0. Indietro");

            int scelta = leggiIntero();
            if (scelta == 1) {
                creaNuovoTeam();
            }
        } else {
            System.out.println("I tuoi team:");
            for (int i = 0; i < mieiTeam.size(); i++) {
                System.out.println((i+1) + ". " + mieiTeam.get(i).getNomeTeam() +
                        " (Membri: " + mieiTeam.get(i).getNumeroMembri() + ")");
            }

            System.out.println("\n1. Crea nuovo team");
            System.out.println("2. Gestisci team esistente");
            System.out.println("0. Indietro");

            int scelta = leggiIntero();
            if (scelta == 1) {
                creaNuovoTeam();
            } else if (scelta == 2) {
                System.out.print("Seleziona numero team: ");
                int idx = leggiIntero() - 1;
                if (idx >= 0 && idx < mieiTeam.size()) {
                    gestisciTeamSpecifico(mieiTeam.get(idx));
                }
            }
        }
    }

    private void creaNuovoTeam() {
        System.out.print("Nome del team: ");
        String nomeTeam = scanner.nextLine();

        Long nuovoId = System.currentTimeMillis();
        Team nuovoTeam = new Team(nuovoId, nomeTeam);
        nuovoTeam.aggiungiMembro(utenteCorrente);
        teamRepository.save(nuovoTeam);

        System.out.println("Team '" + nomeTeam + "' creato con successo!");
    }

    private void gestisciTeamSpecifico(Team team) {
        System.out.println("\nTeam: " + team.getNomeTeam());
        System.out.println("Membri:");
        for (Utente membro : team.getMembri()) {
            System.out.println("  - " + membro.getNome() + " (" + membro.getEmail() + ")");
        }

        System.out.println("\n1. Invita membro (simulato)");
        System.out.println("2. Visualizza iscrizioni del team");
        System.out.println("0. Indietro");

        int scelta = leggiIntero();
        if (scelta == 1) {
            System.out.print("Email del membro da invitare: ");
            String email = scanner.nextLine();
            System.out.println("Invito inviato a " + email + " (simulato)");
        } else if (scelta == 2) {
            visualizzaIscrizioniTeam(team);
        }
    }

    private void hackathonDisponibili() {
        System.out.println("\n--- HACKATHON DISPONIBILI ---");

        List<Hackathon> hackathonList = hackathonRepository.findAll();

        if (hackathonList.isEmpty()) {
            System.out.println("Nessun hackathon disponibile al momento.");
            return;
        }

        for (int i = 0; i < hackathonList.size(); i++) {
            Hackathon h = hackathonList.get(i);
            System.out.println((i+1) + ". " + h.getNome() +
                    " [Stato: " + h.getStato().getNome() + "]");
            System.out.println("   Max team size: " + h.getMaxTeamSize());
        }

        System.out.println("\n1. Iscrivi un team a un hackathon");
        System.out.println("0. Indietro");

        int scelta = leggiIntero();
        if (scelta == 1) {
            iscriviTeamAHackathon(hackathonList);
        }
    }

    private void iscriviTeamAHackathon(List<Hackathon> hackathonList) {
        System.out.print("Seleziona numero hackathon: ");
        int idxH = leggiIntero() - 1;
        if (idxH < 0 || idxH >= hackathonList.size()) {
            System.out.println("Hackathon non valido.");
            return;
        }

        Hackathon hackathon = hackathonList.get(idxH);

        List<Team> mieiTeam = teamRepository.findByMembro(utenteCorrente);
        if (mieiTeam.isEmpty()) {
            System.out.println("Devi prima creare un team.");
            return;
        }

        System.out.println("Scegli il team da iscrivere:");
        for (int i = 0; i < mieiTeam.size(); i++) {
            System.out.println((i+1) + ". " + mieiTeam.get(i).getNomeTeam());
        }

        int idxT = leggiIntero() - 1;
        if (idxT < 0 || idxT >= mieiTeam.size()) {
            System.out.println("Team non valido.");
            return;
        }

        Team team = mieiTeam.get(idxT);

        try {
            teamService.iscriviTeam(team.getId(), hackathon.getId());
            System.out.println("Team iscritto con successo a " + hackathon.getNome());
        } catch (HackathonNonInIscrizioneException e) {
            System.out.println("X" + e.getMessage());
        } catch (TeamGiaIscrittoException e) {
            System.out.println("X" + e.getMessage());
        } catch (TeamTroppoGrandeException e) {
            System.out.println("X" + e.getMessage());
        } catch (Exception e) {
            System.out.println("X Errore: " + e.getMessage());
        }
    }

    private void leMieIscrizioni() {
        System.out.println("\n--- LE MIE ISCRIZIONI ---");

        List<Team> mieiTeam = teamRepository.findByMembro(utenteCorrente);

        if (mieiTeam.isEmpty()) {
            System.out.println("Non sei in nessun team.");
            return;
        }

        for (Team team : mieiTeam) {
            System.out.println("\nTeam: " + team.getNomeTeam());

            List<Iscrizione> iscrizioni = iscrizioneRepository.findByTeamId(team.getId());

            if (iscrizioni.isEmpty()) {
                System.out.println("  Nessuna iscrizione");
            } else {
                for (Iscrizione i : iscrizioni) {
                    Hackathon h = hackathonRepository.findById(i.getHackathonId()).orElse(null);
                    if (h != null) {
                        System.out.println("  - " + h.getNome() + " (" + h.getStato().getNome() + ")");


                        var sottomissione = sottomissioneRepository
                                .findByHackathonIdAndTeamId(h.getId(), team.getId());
                        if (sottomissione.isPresent()) {
                            System.out.println("Sottomissione inviata: " +
                                    sottomissione.get().getContenuto());
                        } else if (h.getStato().puoInviareSottomissione()) {
                            System.out.println("Sottomissione non ancora inviata");
                        }
                    }
                }
            }
        }
    }

    private void inviaSottomissione() {
        System.out.println("\n--- INVIA SOTTOMISSIONE ---");

        List<Team> mieiTeam = teamRepository.findByMembro(utenteCorrente);

        for (Team team : mieiTeam) {
            List<Iscrizione> iscrizioni = iscrizioneRepository.findByTeamId(team.getId());

            for (Iscrizione i : iscrizioni) {
                Hackathon h = hackathonRepository.findById(i.getHackathonId()).orElse(null);
                if (h != null && h.getStato().puoInviareSottomissione()) {
                    var sottomissioneEsistente = sottomissioneRepository
                            .findByHackathonIdAndTeamId(h.getId(), team.getId());

                    if (sottomissioneEsistente.isEmpty()) {
                        System.out.println("\nTeam '" + team.getNomeTeam() +
                                "' può inviare sottomissione per: " + h.getNome());
                        System.out.print("Inserisci URL del progetto: ");
                        String url = scanner.nextLine();

                        try {
                            submissionService.inviaSottomissione(h.getId(), team.getId(), url);
                            System.out.println("Sottomissione inviata con successo!");
                        } catch (Exception e) {
                            System.out.println("Errore: " + e.getMessage());
                        }
                    }
                }
            }
        }
    }

    private void richiediSupporto() {
        System.out.println("\n--- RICHIESTA SUPPORTO A MENTORE ---");


        Mentore mentore = (Mentore) utenteRepository.findMentore().orElse(null);

        if (mentore == null) {
            System.out.println("Nessun mentore disponibile al momento.");
            return;
        }

        List<Team> mieiTeam = teamRepository.findByMembro(utenteCorrente);
        if (mieiTeam.isEmpty()) {
            System.out.println("Non hai team a cui richiedere supporto.");
            return;
        }

        System.out.println("Scegli il team:");
        for (int i = 0; i < mieiTeam.size(); i++) {
            System.out.println((i+1) + ". " + mieiTeam.get(i).getNomeTeam());
        }

        int idx = leggiIntero() - 1;
        if (idx < 0 || idx >= mieiTeam.size()) {
            System.out.println("Team non valido.");
            return;
        }

        Team team = mieiTeam.get(idx);

        try {
            String linkCall = mentoreService.proponiCall(mentore, team.getId());
            System.out.println("Richiesta inviata! Link call generato: " + linkCall);
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    private void visualizzaValutazioni() {
        System.out.println("\n--- VALUTAZIONI ---");

        List<Team> mieiTeam = teamRepository.findByMembro(utenteCorrente);

        for (Team team : mieiTeam) {
            List<Valutazione> valutazioni = valutazioneRepository.findByTeamId(team.getId());

            if (!valutazioni.isEmpty()) {
                System.out.println("\nTeam: " + team.getNomeTeam());
                for (Valutazione v : valutazioni) {
                    Hackathon h = hackathonRepository.findById(v.getHackathonId()).orElse(null);
                    if (h != null) {
                        System.out.println("  Hackathon: " + h.getNome());
                        System.out.println("  Punteggio: " + v.getPunteggio() + "/10");
                        System.out.println("  Commento: " + v.getCommento());
                    }
                }
            }
        }
    }

    private void areaStaff() {
        if (!(utenteCorrente instanceof MembroStaff)) {
            System.out.println("Area riservata allo staff.");
            return;
        }

        System.out.println("\n--- AREA STAFF ---");

        if (utenteCorrente instanceof Organizzatore) {
            menuOrganizzatore();
        } else if (utenteCorrente instanceof Giudice) {
            menuGiudice();
        } else if (utenteCorrente instanceof Mentore) {
            menuMentore();
        }
    }

    private void menuOrganizzatore() {
        System.out.println("1. Crea nuovo hackathon");
        System.out.println("2. Aggiungi mentore a hackathon");
        System.out.println("3. Proclama vincitore");
        System.out.println("0. Indietro");

        int scelta = leggiIntero();

        switch (scelta) {
            case 1:
                creaHackathon();
                break;
            case 2:
                aggiungiMentore();
                break;
            case 3:
                proclamaVincitore();
                break;
        }
    }

    private void menuGiudice() {
        System.out.println("1. Visualizza sottomissioni da valutare");
        System.out.println("2. Valuta sottomissione");
        System.out.println("0. Indietro");

        int scelta = leggiIntero();

        if (scelta == 1) {
            visualizzaSottomissioniDaValutare();
        } else if (scelta == 2) {
            valutaSottomissione();
        }
    }

    private void menuMentore() {
        System.out.println("1. Visualizza richieste supporto");
        System.out.println("2. Segnala violazione regolamento");
        System.out.println("0. Indietro");

        int scelta = leggiIntero();

        if (scelta == 1) {
            visualizzaRichiesteSupporto();
        } else if (scelta == 2) {
            segnalaViolazione();
        }
    }

    private void creaHackathon() {
        System.out.println("\n--- CREAZIONE NUOVO HACKATHON ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Dimensione massima team: ");
        int maxSize = leggiIntero();


        Long nuovoId = System.currentTimeMillis();
        Hackathon nuovo = new Hackathon(
                nuovoId,
                nome,
                maxSize,
                new IscrizioneAperta(),
                (Organizzatore) utenteCorrente,
                selezionaGiudice()
        );

        hackathonRepository.save(nuovo);
        System.out.println("✅ Hackathon '" + nome + "' creato con successo!");
    }

    private Giudice selezionaGiudice() {
        return new Giudice(2L, "Giudice Default", "giudice@hackhub.it", "password");
    }

    private void aggiungiMentore() {
        System.out.println("Funzionalità da implementare completamente");
    }

    private void proclamaVincitore() {
        System.out.println("\n--- PROCLAMA VINCITORE ---");

        List<Hackathon> hackathonInValutazione = hackathonRepository
                .findByStato(new InValutazione());

        if (hackathonInValutazione.isEmpty()) {
            System.out.println("Nessun hackathon in fase di valutazione.");
            return;
        }

        System.out.println("Scegli hackathon:");
        for (int i = 0; i < hackathonInValutazione.size(); i++) {
            System.out.println((i+1) + ". " + hackathonInValutazione.get(i).getNome());
        }

        int idx = leggiIntero() - 1;
        if (idx >= 0 && idx < hackathonInValutazione.size()) {
            Hackathon h = hackathonInValutazione.get(idx);

            try {
                Long vincitoreId = valutazioneService.proclamaVincitore(
                        h.getId(),
                        new MigliorPunteggioStrategy()
                );

                if (vincitoreId != null) {
                    Team vincitore = teamRepository.findById(vincitoreId).orElse(null);
                    System.out.println("Il vincitore è: " + vincitore.getNomeTeam());
                }
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
    }

    private void visualizzaSottomissioniDaValutare() {
        System.out.println("Funzionalità da implementare completamente");
    }

    private void valutaSottomissione() {
        System.out.println("Funzionalità da implementare completamente");
    }

    private void visualizzaRichiesteSupporto() {
        System.out.println("Funzionalità da implementare completamente");
    }

    private void segnalaViolazione() {
        System.out.println("Funzionalità da implementare completamente");
    }

    private void visualizzaHackathonPubblici() {
        System.out.println("\n--- HACKATHON PUBBLICI ---");
        List<Hackathon> hackathonList = hackathonRepository.findAll();

        if (hackathonList.isEmpty()) {
            System.out.println("Nessun hackathon disponibile.");
            return;
        }

        for (Hackathon h : hackathonList) {
            System.out.println("\n " + h.getNome());
            System.out.println("   Stato: " + h.getStato().getNome());
            System.out.println("   Max team: " + h.getMaxTeamSize());
            System.out.println("   Organizzatore: " + h.getOrganizzatore().getNome());
        }
    }

    private void visualizzaIscrizioniTeam(Team team) {
        System.out.println("\nIscrizioni di " + team.getNomeTeam() + ":");
        List<Iscrizione> iscrizioni = iscrizioneRepository.findByTeamId(team.getId());

        if (iscrizioni.isEmpty()) {
            System.out.println("  Nessuna iscrizione");
        } else {
            for (Iscrizione i : iscrizioni) {
                Hackathon h = hackathonRepository.findById(i.getHackathonId()).orElse(null);
                if (h != null) {
                    System.out.println("  - " + h.getNome());
                }
            }
        }
    }

    private void inizializzaDatiTest() {
        System.out.println("Inizializzazione dati di test...");


        Organizzatore azizOrg = new Organizzatore(1L, "Aziz", "aziz@hackhub.it", "admin");
        Giudice giudiceRossi = new Giudice(2L, "Rossi", "rossi@univ.it", "judge");
        Mentore mentorePiero = new Mentore(3L, "Piero", "piero@mentors.it", "mentor");
        Utente mario = new Utente(101L, "Mario", "mario@email.it", "1234");

        utenteRepository.save(azizOrg);
        utenteRepository.save(giudiceRossi);
        utenteRepository.save(mentorePiero);
        utenteRepository.save(mario);

        Hackathon hackathon = new Hackathon(1L, "Java Challenge 2026", 4,
                new IscrizioneAperta(), azizOrg, giudiceRossi);
        hackathon.aggiungiMentore(mentorePiero);
        hackathonRepository.save(hackathon);

        Team team = new Team(10L, "Dev Team");
        team.aggiungiMembro(mario);
        teamRepository.save(team);

        System.out.println("Dati di test inizializzati!");
    }

    private int leggiIntero() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}