package it.hackhub.cli;

import it.hackhub.controller.*;
import it.hackhub.domain.*;
import it.hackhub.domain.staff.Giudice;
import it.hackhub.domain.staff.MembroStaff;
import it.hackhub.domain.staff.Mentore;
import it.hackhub.domain.staff.Organizzatore;
import it.hackhub.repository.SottomissioneRepository;
import it.hackhub.repository.TeamRepository;
import it.hackhub.repository.UtenteRepository;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
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
                    case "15": invitaUtenteTeam(input); break;
                    case "16": gestisciInvitoTeam(input); break;
                    case "17": consultaHackathonAssegnati(input); break;
                    case "18": consultaSottomissioniTeam(input); break;
                    case "19": segnalaTeam(input); break;
                    case "20": proponiCallSupporto(input); break;
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
        System.out.println("\n=== HackHub CLI - Iterazione Finale ===");
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
        System.out.println("15. Invitare utente nel team");
        System.out.println("16. Accettare o rifiutare invito team");
        System.out.println("17. Consultare hackathon assegnati");
        System.out.println("18. Consultare sottomissioni del proprio team");
        System.out.println("19. Segnalare team all'organizzatore");
        System.out.println("20. Proporre call di supporto");
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
        String aggiorna = input.stringa("Aggiornare una sottomissione già esistente? [s/N]: ");
        if ("s".equalsIgnoreCase(aggiorna)) {
            Long sottomissioneId = input.longValue("ID sottomissione: ");
            Sottomissione s = sottomissioneController.aggiornaSottomissione(sottomissioneId, input.stringa("Nuovo file/link/descrizione: "));
            System.out.println("Sottomissione aggiornata: " + s.getId());
            return;
        }
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
        String nome = input.stringa("Nome hackathon: ");
        String regolamento = input.stringa("Regolamento: ");
        java.time.LocalDateTime scadenza = input.dataOra("Scadenza iscrizioni");
        java.time.LocalDateTime inizio = input.dataOra("Data inizio");
        java.time.LocalDateTime fine = input.dataOra("Data fine");
        String luogo = input.stringa("Luogo: ");
        double premio = input.doubleValue("Premio in denaro: ");
        int maxTeamSize = input.intValue("Numero massimo membri team: ");
        Giudice giudice = null;
        String emailGiudice = input.stringa("Email giudice iniziale [invio per saltare]: ");
        if (!emailGiudice.isBlank()) {
            Utente u = utenteRepository.findByEmail(emailGiudice).orElseThrow(() -> new IllegalArgumentException("Giudice non trovato"));
            if (!(u instanceof Giudice)) {
                throw new IllegalArgumentException("L'utente indicato non è un giudice");
            }
            giudice = (Giudice) u;
        }
        List<Mentore> mentori = new ArrayList<>();
        while (true) {
            String emailMentore = input.stringa("Email mentore iniziale [invio per terminare]: ");
            if (emailMentore.isBlank()) {
                break;
            }
            Utente u = utenteRepository.findByEmail(emailMentore).orElseThrow(() -> new IllegalArgumentException("Mentore non trovato"));
            if (!(u instanceof Mentore)) {
                throw new IllegalArgumentException("L'utente indicato non è un mentore");
            }
            mentori.add((Mentore) u);
        }
        Hackathon h = hackathonController.creaHackathon(nome, regolamento, scadenza, inizio, fine, luogo, premio, maxTeamSize, (Organizzatore) utente, giudice, mentori);
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

    private void invitaUtenteTeam(LettoreInput input) {
        Utente utente = richiediUtenteCorrente(input);
        visualizzaTeam();
        InvitoTeam invito = teamController.invitaUtente(
                input.longValue("ID team: "),
                utente.getId(),
                input.stringa("Email utente da invitare: "));
        System.out.println("Invito creato: " + invito.getId() + " - stato: " + invito.getStato());
    }

    private void gestisciInvitoTeam(LettoreInput input) {
        Utente utente = richiediUtenteCorrente(input);
        System.out.println("\n--- Inviti ricevuti ---");
        teamController.getInvitiUtente(utente.getId()).forEach(i -> System.out.println(i.getId()
                + " - Team ID: " + i.getTeamId()
                + " - stato: " + i.getStato()));
        Long invitoId = input.longValue("ID invito: ");
        String scelta = input.stringa("Accettare invito? [s/N]: ");
        if ("s".equalsIgnoreCase(scelta)) {
            teamController.accettaInvito(invitoId, utente.getId());
            System.out.println("Invito accettato. Benvenuto nel team.");
        } else {
            teamController.rifiutaInvito(invitoId, utente.getId());
            System.out.println("Invito rifiutato.");
        }
    }

    private void consultaHackathonAssegnati(LettoreInput input) {
        Long staffId = input.longValue("ID membro staff: ");
        System.out.println("\n--- Hackathon assegnati ---");
        hackathonController.consultaHackathonAssegnati(staffId)
                .forEach(h -> System.out.println(h.getId() + " - " + h.getNome() + " - stato: " + h.getStato()));
    }

    private void consultaSottomissioniTeam(LettoreInput input) {
        Long teamId = input.longValue("ID team: ");
        Long hackathonId = input.longValue("ID hackathon: ");
        System.out.println("\n--- Sottomissioni del team ---");
        sottomissioneController.consultaSottomissioni(teamId, hackathonId)
                .forEach(s -> System.out.println(s.getId() + " - " + s.getContenuto() + " - valutata: " + s.risultaValutata()));
    }

    private void segnalaTeam(LettoreInput input) {
        RichiestaSupporto segnalazione = supportoController.inoltraSegnalazione(
                input.longValue("ID mentore: "),
                input.longValue("ID team da segnalare: "),
                input.longValue("ID hackathon: "),
                input.stringa("Motivazione: "));
        System.out.println("Segnalazione inviata all'organizzatore: " + segnalazione.getId());
    }

    private void proponiCallSupporto(LettoreInput input) {
        CallSupporto call = supportoController.proponiCallSupporto(
                input.longValue("ID richiesta supporto: "),
                input.longValue("ID mentore: "),
                input.dataOra("Data e ora call"));
        System.out.println("Call pianificata: " + call.getLink() + " alle " + call.getDataOra());
    }

    private void visualizzaTeam() {
        System.out.println("\n--- Team ---");
        teamController.visualizzaTeam().forEach(t -> System.out.println(t.getId() + " - " + t.getNomeTeam() + " - membri: " + t.getDimensioneTeam()));
    }

    private void visualizzaSottomissioni() {
        System.out.println("\n--- Sottomissioni ---");
        if (utenteCorrente instanceof MembroStaff) {
            Long hackathonId = null;
            List<Hackathon> assegnati = hackathonController.consultaHackathonAssegnati(utenteCorrente.getId());
            if (!assegnati.isEmpty()) {
                assegnati.forEach(h -> System.out.println("Hackathon assegnato: " + h.getId() + " - " + h.getNome()));
                try {
                    Scanner scanner = new Scanner(System.in);
                    hackathonId = Long.parseLong(new LettoreInput(scanner).stringa("ID hackathon per filtrare sottomissioni: "));
                } catch (RuntimeException ignored) {
                    hackathonId = assegnati.get(0).getId();
                }
                sottomissioneController.consultaSottomissioniStaff(hackathonId, utenteCorrente.getId()).forEach(s -> System.out.println(s.getId()
                        + " - Team ID: " + s.getTeamId()
                        + " - Hackathon ID: " + s.getHackathonId()
                        + " - Valutata: " + s.risultaValutata()
                        + " - Media: " + s.getPunteggioMedio()
                        + " - " + s.getContenuto()));
                return;
            }
        }
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
