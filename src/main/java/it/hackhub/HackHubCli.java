package it.hackhub;

import it.hackhub.domain.*;
import it.hackhub.domain.staff.*;
import it.hackhub.domain.state.*;
import it.hackhub.domain.strategy.MigliorPunteggioStrategy;
import it.hackhub.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class HackHubCli implements CommandLineRunner {

    @Autowired private TeamService teamService;
    @Autowired private SubmissionService submissionService;
    @Autowired private ValutazioneService valutazioneService;
    @Autowired private MentoreService mentoreService;
    @Autowired private UtenteService utenteService;
    @Autowired private HackathonService hackathonService;

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
            case 1: accedi(); break;
            case 2: registrati(); break;
            case 3: visualizzaHackathonPubblici(); break;
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
            case 1: gestisciTeam(); break;
            case 2: hackathonDisponibili(); break;
            case 3: leMieIscrizioni(); break;
            case 4: inviaSottomissione(); break;
            case 5: richiediSupporto(); break;
            case 6: visualizzaValutazioni(); break;
            case 7: areaStaff(); break;
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

        Optional<Utente> utenteOpt = utenteService.login(email, password);

        if (utenteOpt.isPresent()) {
            utenteCorrente = utenteOpt.get();
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

        utenteService.registraUtente(nome, email, password);

        System.out.println("Registrazione completata! Ora puoi accedere.");
    }

    private void gestisciTeam() {
        System.out.println("\n--- GESTIONE TEAM ---");
        List<Team> mieiTeam = teamService.getTeamDiUtente(utenteCorrente);

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

        try {
            teamService.creaTeam(nomeTeam, utenteCorrente);
            System.out.println("Team '" + nomeTeam + "' creato con successo!");
        } catch (Exception e) {
            System.out.println("Errore durante la creazione del team.");
        }
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
        List<Hackathon> hackathonList = hackathonService.getAllHackathon();

        if (hackathonList.isEmpty()) {
            System.out.println("Nessun hackathon disponibile al momento.");
            return;
        }

        for (int i = 0; i < hackathonList.size(); i++) {
            Hackathon h = hackathonList.get(i);
            System.out.println((i+1) + ". " + h.getNome() + " [Stato: " + h.getStato().getNome() + "]");
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
        List<Team> mieiTeam = teamService.getTeamDiUtente(utenteCorrente);

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

        try {
            teamService.iscriviTeam(mieiTeam.get(idxT).getId(), hackathon.getId());
            System.out.println("Team iscritto con successo a " + hackathon.getNome());
        } catch (Exception e) {
            System.out.println("❌ Errore: " + e.getMessage());
        }
    }

    private void leMieIscrizioni() {
        System.out.println("\n--- LE MIE ISCRIZIONI ---");
        List<Team> mieiTeam = teamService.getTeamDiUtente(utenteCorrente);

        if (mieiTeam.isEmpty()) {
            System.out.println("Non sei in nessun team.");
            return;
        }

        for (Team team : mieiTeam) {
            System.out.println("\nTeam: " + team.getNomeTeam());
            List<Iscrizione> iscrizioni = teamService.getIscrizioniTeam(team.getId());

            if (iscrizioni.isEmpty()) {
                System.out.println("  Nessuna iscrizione");
            } else {
                for (Iscrizione i : iscrizioni) {
                    Hackathon h = hackathonService.getHackathonById(i.getHackathonId()).orElse(null);
                    if (h != null) {
                        System.out.println("  - " + h.getNome() + " (" + h.getStato().getNome() + ")");
                        Optional<Sottomissione> sottomissione = submissionService.getSottomissioneTeam(h.getId(), team.getId());
                        if (sottomissione.isPresent()) {
                            System.out.println("Sottomissione inviata: " + sottomissione.get().getContenuto());
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
        List<Team> mieiTeam = teamService.getTeamDiUtente(utenteCorrente);

        for (Team team : mieiTeam) {
            List<Iscrizione> iscrizioni = teamService.getIscrizioniTeam(team.getId());
            for (Iscrizione i : iscrizioni) {
                Hackathon h = hackathonService.getHackathonById(i.getHackathonId()).orElse(null);
                if (h != null && h.getStato().puoInviareSottomissione()) {
                    Optional<Sottomissione> sottomissioneEsistente = submissionService.getSottomissioneTeam(h.getId(), team.getId());
                    if (sottomissioneEsistente.isEmpty()) {
                        System.out.println("\nTeam '" + team.getNomeTeam() + "' può inviare sottomissione per: " + h.getNome());
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
        Utente utenteMentore = utenteService.findMentoreDisponibile().orElse(null);

        if (!(utenteMentore instanceof Mentore)) {
            System.out.println("Nessun mentore disponibile al momento.");
            return;
        }

        Mentore mentore = (Mentore) utenteMentore;
        List<Team> mieiTeam = teamService.getTeamDiUtente(utenteCorrente);

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
        mentoreService.creaRichiestaSupporto(team.getId(), team.getNomeTeam(), mentore);
        System.out.println("✅ Richiesta salvata nel sistema e inoltrata al mentore: " + mentore.getNome());
    }

    private void visualizzaValutazioni() {
        System.out.println("\n--- VALUTAZIONI ---");
        List<Team> mieiTeam = teamService.getTeamDiUtente(utenteCorrente);

        for (Team team : mieiTeam) {
            List<Valutazione> valutazioni = valutazioneService.getValutazioniTeam(team.getId());
            if (!valutazioni.isEmpty()) {
                System.out.println("\nTeam: " + team.getNomeTeam());
                for (Valutazione v : valutazioni) {
                    Hackathon h = hackathonService.getHackathonById(v.getHackathonId()).orElse(null);
                    if (h != null) {
                        System.out.println("  Hackathon: " + h.getNome() + " | Punteggio: " + v.getPunteggio() + "/10 | Commento: " + v.getCommento());
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
        if (utenteCorrente instanceof Organizzatore) menuOrganizzatore();
        else if (utenteCorrente instanceof Giudice) menuGiudice();
        else if (utenteCorrente instanceof Mentore) menuMentore();
    }

    private void menuOrganizzatore() {
        System.out.println("1. Crea nuovo hackathon\n2. Aggiungi mentore a hackathon\n3. Cambia stato hackathon\n4. Proclama vincitore\n0. Indietro");
        int scelta = leggiIntero();
        if (scelta == 1) creaHackathon();
        else if (scelta == 2) aggiungiMentore();
        else if (scelta == 3) cambiaStatoHackathon();
        else if (scelta == 4) proclamaVincitore();
    }

    private void menuGiudice() {
        System.out.println("1. Visualizza sottomissioni da valutare\n2. Valuta sottomissione\n0. Indietro");
        int scelta = leggiIntero();
        if (scelta == 1) visualizzaSottomissioniDaValutare();
        else if (scelta == 2) valutaSottomissione();
    }

    private void menuMentore() {
        System.out.println("1. Visualizza richieste supporto\n2. Segnala violazione regolamento\n0. Indietro");
        int scelta = leggiIntero();
        if (scelta == 1) visualizzaRichiesteSupporto();
        else if (scelta == 2) segnalaViolazione();
    }

    private void creaHackathon() {
        System.out.println("\n--- CREAZIONE NUOVO HACKATHON ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Dimensione massima team: ");
        int maxSize = leggiIntero();

        hackathonService.creaHackathon(nome, maxSize, (Organizzatore) utenteCorrente, selezionaGiudice());
        System.out.println("✅ Hackathon '" + nome + "' creato.");
    }

    private Giudice selezionaGiudice() {
        return new Giudice(2L, "Giudice Default", "giudice@hackhub.it", "password");
    }

    private void aggiungiMentore() {
        System.out.println("\n--- AGGIUNGI MENTORE ---");
        List<Hackathon> hackathonList = hackathonService.getAllHackathon();
        for (int i = 0; i < hackathonList.size(); i++) {
            System.out.println((i+1) + ". " + hackathonList.get(i).getNome());
        }
        int idxH = leggiIntero() - 1;
        if (idxH < 0 || idxH >= hackathonList.size()) return;

        Hackathon hSelected = hackathonList.get(idxH);
        System.out.print("Email mentore: ");
        String email = scanner.nextLine();
        Utente u = utenteService.findByEmail(email).orElse(null);

        if (u instanceof Mentore) {
            hackathonService.aggiungiMentore(hSelected.getId(), (Mentore) u);
            System.out.println("✅ Mentore aggiunto.");
        } else {
            System.out.println("❌ Non trovato.");
        }
    }

    private void cambiaStatoHackathon() {
        System.out.println("\n--- CAMBIA STATO HACKATHON ---");
        List<Hackathon> list = hackathonService.getAllHackathon();
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ". " + list.get(i).getNome() + " (Stato attuale: " + list.get(i).getStato().getNome() + ")");
        }

        System.out.print("Seleziona hackathon: ");
        int idx = leggiIntero() - 1;
        if (idx < 0 || idx >= list.size()) return;

        System.out.println("Scegli nuovo stato:\n1. In Iscrizione\n2. In Corso\n3. In Valutazione\n4. Concluso");
        int statoScelta = leggiIntero();
        StatoHackathon nuovoStato = null;

        switch (statoScelta) {
            case 1: nuovoStato = new IscrizioneAperta(); break;
            case 2: nuovoStato = new InCorso(); break;
            case 3: nuovoStato = new InValutazione(); break;
            case 4: nuovoStato = new Concluso(); break;
            default: System.out.println("Scelta non valida."); return;
        }

        hackathonService.cambiaStato(list.get(idx).getId(), nuovoStato);
        System.out.println("✅ Stato aggiornato con successo.");
    }

    private void proclamaVincitore() {
        System.out.println("\n--- PROCLAMA VINCITORE ---");
        List<Hackathon> inVal = hackathonService.getHackathonByStato(new InValutazione());
        if (inVal.isEmpty()) { System.out.println("Nessuno in valutazione."); return; }

        for (int i = 0; i < inVal.size(); i++) System.out.println((i+1) + ". " + inVal.get(i).getNome());
        int idx = leggiIntero() - 1;
        if (idx >= 0 && idx < inVal.size()) {
            try {
                Long vId = valutazioneService.proclamaVincitore(inVal.get(idx).getId(), new MigliorPunteggioStrategy());
                if (vId != null) {
                    Team v = teamService.getTeamById(vId).orElse(null);
                    if (v != null) System.out.println("🏆 Il vincitore è: " + v.getNomeTeam());
                }
            } catch (Exception e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
    }

    private void visualizzaSottomissioniDaValutare() {
        System.out.println("\n--- SOTTOMISSIONI DA VALUTARE ---");
        List<Hackathon> tutti = hackathonService.getAllHackathon();
        boolean trovate = false;

        for (Hackathon h : tutti) {
            if (h.getGiudice() != null && h.getGiudice().getId().equals(utenteCorrente.getId())) {
                System.out.println("\nHackathon: " + h.getNome());
                List<Iscrizione> iscrizioni = hackathonService.getIscrizioniHackathon(h.getId());
                for (Iscrizione i : iscrizioni) {
                    Optional<Sottomissione> sottomissioneOpt = submissionService.getSottomissioneTeam(h.getId(), i.getTeamId());
                    if (sottomissioneOpt.isPresent()) {
                        System.out.println("  - Team ID: " + i.getTeamId() + " | Progetto: " + sottomissioneOpt.get().getContenuto());
                        trovate = true;
                    }
                }
            }
        }
        if (!trovate) System.out.println("Nessuna sottomissione da valutare.");
    }

    private void valutaSottomissione() {
        System.out.println("\n--- VALUTA SOTTOMISSIONE ---");
        System.out.print("ID Hackathon: "); Long hId = (long) leggiIntero();
        System.out.print("ID Team: "); Long tId = (long) leggiIntero();
        System.out.print("Punteggio (0-10): "); int p = leggiIntero();
        System.out.print("Commento: "); String c = scanner.nextLine();

        try {
            valutazioneService.valutaTeam(hId, tId, p, c);
            System.out.println("✅ Valutata.");
        } catch (Exception e) {
            System.out.println("❌ Errore: " + e.getMessage());
        }
    }

    private void visualizzaRichiesteSupporto() {
        System.out.println("\n--- RICHIESTE DI SUPPORTO ASSEGNATE ---");
        List<RichiestaSupporto> richieste = mentoreService.getRichiestePendenti(utenteCorrente.getId());

        if (richieste.isEmpty()) {
            System.out.println("Non ci sono richieste di supporto pendenti per te.");
            return;
        }

        for (int i = 0; i < richieste.size(); i++) {
            System.out.println((i+1) + ". Richiesta ID: " + richieste.get(i).getId() + " | Team: " + richieste.get(i).getNomeTeam());
        }

        System.out.println("\n1. Accetta richiesta e genera link call\n0. Indietro");
        int scelta = leggiIntero();

        if (scelta == 1) {
            System.out.print("Seleziona numero indice della richiesta: ");
            int idx = leggiIntero() - 1;
            if (idx >= 0 && idx < richieste.size()) {
                try {
                    String link = mentoreService.accettaRichiestaEPrenotaCall(richieste.get(idx).getId());
                    System.out.println("✅ Call confermata! Link: " + link);
                } catch (Exception e) {
                    System.out.println("❌ Errore: " + e.getMessage());
                }
            }
        }
    }

    private void segnalaViolazione() {
        System.out.println("\n--- SEGNALA VIOLAZIONE ---");
        System.out.print("ID Team: "); String id = scanner.nextLine();
        System.out.print("Descrizione: "); String desc = scanner.nextLine();
        System.out.println("✅ Inviata agli organizzatori per il Team: " + id);
    }

    private void visualizzaHackathonPubblici() {
        List<Hackathon> list = hackathonService.getAllHackathon();
        if (list.isEmpty()) { System.out.println("Nessuno disponibile."); return; }
        for (Hackathon h : list) {
            System.out.println("\n " + h.getNome() + " | Stato: " + h.getStato().getNome() + " | Org: " + h.getOrganizzatore().getNome());
        }
    }

    private void visualizzaIscrizioniTeam(Team team) {
        List<Iscrizione> iscrizioni = teamService.getIscrizioniTeam(team.getId());
        if (iscrizioni.isEmpty()) System.out.println("  Nessuna iscrizione");
        for (Iscrizione i : iscrizioni) {
            hackathonService.getHackathonById(i.getHackathonId()).ifPresent(h -> System.out.println("  - " + h.getNome()));
        }
    }

    private void inizializzaDatiTest() {
        System.out.println("Inizializzazione dati di test...");
        Organizzatore azizOrg = new Organizzatore(1L, "Aziz", "aziz@hackhub.it", "admin");
        Giudice giudiceRossi = new Giudice(2L, "Rossi", "rossi@univ.it", "judge");
        Mentore mentorePiero = new Mentore(3L, "Piero", "piero@mentors.it", "mentor");
        Utente mario = new Utente(101L, "Mario", "mario@email.it", "1234");

        utenteService.salvaUtente(azizOrg);
        utenteService.salvaUtente(giudiceRossi);
        utenteService.salvaUtente(mentorePiero);
        utenteService.salvaUtente(mario);

        Hackathon hackathon = new Hackathon(1L, "Java Challenge 2026", 4, new IscrizioneAperta(), azizOrg, giudiceRossi);
        hackathon.aggiungiMentore(mentorePiero);
        hackathonService.salvaHackathon(hackathon);

        Team team = new Team(10L, "Dev Team");
        team.aggiungiMembro(mario);
        teamService.salvaTeam(team);

        System.out.println("Dati di test pronti!");
    }

    private int leggiIntero() {
        try { return Integer.parseInt(scanner.nextLine()); } catch (NumberFormatException e) { return -1; }
    }
}