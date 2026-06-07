package it.hackhub.cli;

import it.hackhub.controller.*;
import it.hackhub.domain.*;
import it.hackhub.domain.staff.Giudice;
import it.hackhub.domain.staff.Organizzatore;
import it.hackhub.repository.SottomissioneRepository;
import it.hackhub.repository.TeamRepository;
import it.hackhub.repository.UtenteRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.Scanner;

@Component
public class MainMenu {
    private final AutenticazioneController autenticazioneController;
    private final ProfiloController profiloController;
    private final TeamController teamController;
    private final IscrizioneController iscrizioneController;
    private final SottomissioneController sottomissioneController;
    private final ValutazioneController valutazioneController;
    private final SupportoController supportoController;
    private final HackathonController hackathonController;
    private final UtenteRepository utenteRepository;
    private final TeamRepository teamRepository;
    private final SottomissioneRepository sottomissioneRepository;

    private Utente utenteCorrente;

    public MainMenu(AutenticazioneController autenticazioneController,
                    ProfiloController profiloController,
                    TeamController teamController,
                    IscrizioneController iscrizioneController,
                    SottomissioneController sottomissioneController,
                    ValutazioneController valutazioneController,
                    SupportoController supportoController,
                    HackathonController hackathonController,
                    UtenteRepository utenteRepository,
                    TeamRepository teamRepository,
                    SottomissioneRepository sottomissioneRepository) {
        this.autenticazioneController = autenticazioneController;
        this.profiloController = profiloController;
        this.teamController = teamController;
        this.iscrizioneController = iscrizioneController;
        this.sottomissioneController = sottomissioneController;
        this.valutazioneController = valutazioneController;
        this.supportoController = supportoController;
        this.hackathonController = hackathonController;
        this.utenteRepository = utenteRepository;
        this.teamRepository = teamRepository;
        this.sottomissioneRepository = sottomissioneRepository;
    }

    public void avvia() {
        Scanner scanner = new Scanner(System.in);
        LettoreInput input = new LettoreInput(scanner);
        while (true) {
            stampaMenu();
            String scelta = input.stringa("Scelta: ");
            try {
                switch (scelta) {
                    case "1": registra(input); break;
                    case "2": login(input); break;
                    case "3": visualizzaHackathon(); break;
                    case "4": gestioneProfilo(input); break;
                    case "5": creaTeam(input); break;
                    case "6": iscriviTeam(input); break;
                    case "7": inviaSottomissione(input); break;
                    case "8": valutaSottomissione(input); break;
                    case "9": apriRichiestaSupporto(input); break;
                    case "10": visualizzaRichiesteSupporto(input); break;
                    case "11": assegnaStaff(input); break;
                    case "12": creaHackathon(input); break;
                    case "13": proclamaVincitore(input); break;
                    case "14": visualizzaSottomissioni(); break;
                    case "0": System.out.println("Chiusura HackHub CLI."); return;
                    default: System.out.println("Scelta non valida.");
                }
            } catch (RuntimeException e) {
                System.out.println("Errore: " + e.getMessage());
            }
        }
    }

    private void stampaMenu() {
        String login = utenteCorrente == null ? "nessun utente" : utenteCorrente.getNome() + " <" + utenteCorrente.getEmail() + ">";
        System.out.println("\n=== HackHub CLI - Terza Iterazione ===");
        System.out.println("Utente corrente: " + login);
        System.out.println("1. Registrarsi alla piattaforma");
        System.out.println("2. Accedere alla piattaforma");
        System.out.println("3. Consultare hackathon");
        System.out.println("4. Gestire profilo");
        System.out.println("5. Creare team");
        System.out.println("6. Iscrivere team a hackathon");
        System.out.println("7. Inviare sottomissione");
        System.out.println("8. Valutare sottomissione");
        System.out.println("9. Inviare richiesta supporto");
        System.out.println("10. Visualizzare richieste supporto");
        System.out.println("11. Assegnare giudice o mentore");
        System.out.println("12. Creare hackathon");
        System.out.println("13. Proclamare vincitore");
        System.out.println("14. Visualizzare sottomissioni");
        System.out.println("0. Esci");
    }

    private void registra(LettoreInput input) {
        Utente utente = autenticazioneController.registra(
                input.stringa("Nome: "),
                input.stringa("Email: "),
                input.stringa("Password: "));
        utenteCorrente = utente;
        System.out.println("Registrazione completata: " + utente.getDatiBase());
    }

    private void login(LettoreInput input) {
        Optional<Utente> utente = autenticazioneController.accedi(input.stringa("Email: "), input.stringa("Password: "));
        if (utente.isEmpty()) {
            System.out.println("Credenziali non valide.");
            return;
        }
        utenteCorrente = utente.get();
        System.out.println("Accesso effettuato: " + utenteCorrente.getDatiBase());
    }

    private void visualizzaHackathon() {
        System.out.println("\n--- Hackathon ---");
        hackathonController.consultaHackathon().forEach(h -> {
            String vincitore = h.getTeamVincitore() == null ? "nessuno" : h.getTeamVincitore().getNomeTeam();
            System.out.println(h.getId() + " - " + h.getNome() + " - stato: " + h.getStato()
                    + " - max team: " + h.getMaxTeamSize() + " - vincitore: " + vincitore);
        });
    }

    private void gestioneProfilo(LettoreInput input) {
        Utente utente = richiediUtenteCorrente(input);
        System.out.println("Profilo: " + utente.getDatiBase());
        profiloController.recuperaTeamIscritti(utente).forEach(t -> System.out.println("Team: " + t.getNomeTeam()));
        String modifica = input.stringa("Vuoi modificare nome/email? [s/N]: ");
        if ("s".equalsIgnoreCase(modifica)) {
            utenteCorrente = profiloController.aggiornaProfilo(utente.getId(), input.stringa("Nuovo nome: "), input.stringa("Nuova email: "));
            System.out.println("Profilo aggiornato: " + utenteCorrente.getDatiBase());
        }
    }

    private void creaTeam(LettoreInput input) {
        Utente creatore = richiediUtenteCorrente(input);
        Team team = teamController.creaTeam(input.stringa("Nome team: "), creatore);
        System.out.println("Team creato: " + team.getId() + " - " + team.getNomeTeam());
    }

    private void iscriviTeam(LettoreInput input) {
        visualizzaTeam();
        Long teamId = input.longValue("ID team: ");
        visualizzaHackathon();
        Long hackathonId = input.longValue("ID hackathon: ");
        System.out.println(iscrizioneController.iscriviTeam(teamId, hackathonId));
    }

    private void inviaSottomissione(LettoreInput input) {
        visualizzaTeam();
        Long teamId = input.longValue("ID team: ");
        visualizzaHackathon();
        Long hackathonId = input.longValue("ID hackathon: ");
        Sottomissione s = sottomissioneController.inviaSottomissione(teamId, hackathonId, input.stringa("File/link/descrizione: "));
        System.out.println("Sottomissione inviata: " + s.getId());
    }

    private void valutaSottomissione(LettoreInput input) {
        visualizzaSottomissioni();
        Valutazione v = valutazioneController.inviaValutazione(
                input.longValue("ID sottomissione: "),
                input.longValue("ID giudice: "),
                input.intValue("Voto 0-10: "),
                input.stringa("Commento: "));
        System.out.println("Valutazione salvata: " + v.getId() + " - punteggio " + v.getPunteggio());
    }

    private void apriRichiestaSupporto(LettoreInput input) {
        RichiestaSupporto r = supportoController.inviaDatiRichiesta(
                input.longValue("ID utente richiedente: "),
                input.longValue("ID team: "),
                input.longValue("ID hackathon: "),
                input.stringa("Topic: "),
                input.stringa("Descrizione: "),
                input.stringa("Priorità [BASSA/MEDIA/ALTA]: "));
        System.out.println("Richiesta supporto aperta: " + r.getId());
    }

    private void visualizzaRichiesteSupporto(LettoreInput input) {
        supportoController.visualizzaRichiesteAperte().forEach(r -> System.out.println(r.getId()
                + " - Team ID: " + r.getTeamId()
                + " - Hackathon ID: " + r.getHackathonId()
                + " - " + r.getTopic()
                + " - priorità: " + r.getPriorita()));
    }

    private void assegnaStaff(LettoreInput input) {
        visualizzaHackathon();
        hackathonController.assegnaStaff(input.longValue("ID hackathon: "), input.stringa("Email utente staff: "), input.stringa("Ruolo [GIUDICE/MENTORE]: "));
        System.out.println("Staff assegnato correttamente.");
    }

    private void creaHackathon(LettoreInput input) {
        Utente utente = richiediUtenteCorrente(input);
        if (!(utente instanceof Organizzatore)) {
            throw new IllegalStateException("Solo un organizzatore può creare hackathon");
        }
        Hackathon h = hackathonController.creaHackathon(
                input.stringa("Nome hackathon: "),
                input.stringa("Regolamento: "),
                input.dataOra("Scadenza iscrizioni"),
                input.dataOra("Data inizio"),
                input.dataOra("Data fine"),
                input.intValue("Numero massimo membri team: "),
                (Organizzatore) utente);
        System.out.println("Hackathon creato: " + h.getId() + " - " + h.getNome());
    }

    private void proclamaVincitore(LettoreInput input) {
        visualizzaHackathon();
        Long hackathonId = input.longValue("ID hackathon: ");
        System.out.println("Classifica:");
        hackathonController.calcolaClassifica(hackathonId)
                .forEach(e -> System.out.println(e.getTeam().getId() + " - " + e.getTeam().getNomeTeam() + " - media: " + e.getPunteggioMedio()));
        String automatico = input.stringa("Proclamazione automatica? [S/n]: ");
        Team vincitore = "n".equalsIgnoreCase(automatico)
                ? hackathonController.proclamaVincitore(hackathonId, input.longValue("ID team vincitore: "))
                : hackathonController.proclamaVincitoreAutomaticamente(hackathonId);
        System.out.println("Vincitore proclamato: " + vincitore.getNomeTeam());
    }

    private void visualizzaTeam() {
        System.out.println("\n--- Team ---");
        teamController.visualizzaTeam().forEach(t -> System.out.println(t.getId() + " - " + t.getNomeTeam() + " - membri: " + t.getDimensioneTeam()));
    }

    private void visualizzaSottomissioni() {
        System.out.println("\n--- Sottomissioni ---");
        sottomissioneRepository.findAll().forEach(s -> System.out.println(s.getId()
                + " - Team ID: " + s.getTeamId()
                + " - Hackathon ID: " + s.getHackathonId()
                + " - Valutata: " + s.risultaValutata()
                + " - Media: " + s.getPunteggioMedio()
                + " - " + s.getContenuto()));
    }

    private Utente richiediUtenteCorrente(LettoreInput input) {
        if (utenteCorrente != null) {
            return utenteCorrente;
        }
        Long id = input.longValue("ID utente: ");
        utenteCorrente = utenteRepository.findById(id).orElseThrow(() -> new RuntimeException("Utente non trovato"));
        return utenteCorrente;
    }
}
