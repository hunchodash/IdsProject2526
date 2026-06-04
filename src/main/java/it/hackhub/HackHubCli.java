package it.hackhub;

import it.hackhub.domain.*;
import it.hackhub.domain.staff.Giudice;
import it.hackhub.domain.staff.Mentore;
import it.hackhub.domain.staff.Organizzatore;
import it.hackhub.repository.HackathonRepository;
import it.hackhub.repository.SottomissioneRepository;
import it.hackhub.repository.TeamRepository;
import it.hackhub.repository.UtenteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class HackHubCli implements CommandLineRunner {

    private final UtenteRepository utenteRepository;
    private final TeamRepository teamRepository;
    private final HackathonRepository hackathonRepository;
    private final SottomissioneRepository sottomissioneRepository;

    private final List<RichiestaSupporto> richiesteSupporto = new ArrayList<>();

    public HackHubCli(UtenteRepository utenteRepository,
                      TeamRepository teamRepository,
                      HackathonRepository hackathonRepository,
                      SottomissioneRepository sottomissioneRepository) {
        this.utenteRepository = utenteRepository;
        this.teamRepository = teamRepository;
        this.hackathonRepository = hackathonRepository;
        this.sottomissioneRepository = sottomissioneRepository;
    }

    @Override
    public void run(String... args) {
        caricaDatiDimostrativi();
        avviaMenu();
    }

    private void avviaMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== HackHub CLI - Seconda Iterazione ===");
            System.out.println("1. Visualizza utenti");
            System.out.println("2. Visualizza team");
            System.out.println("3. Visualizza hackathon");
            System.out.println("4. Visualizza sottomissioni");
            System.out.println("5. Crea team");
            System.out.println("6. Iscrivi team a hackathon");
            System.out.println("7. Invia sottomissione");
            System.out.println("8. Valuta sottomissione");
            System.out.println("9. Apri richiesta supporto");
            System.out.println("10. Visualizza richieste supporto");
            System.out.println("11. Proclama vincitore");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");

            String scelta = scanner.nextLine();

            switch (scelta) {
                case "1":
                    visualizzaUtenti();
                    break;
                case "2":
                    visualizzaTeam();
                    break;
                case "3":
                    visualizzaHackathon();
                    break;
                case "4":
                    visualizzaSottomissioni();
                    break;
                case "5":
                    creaTeam(scanner);
                    break;
                case "6":
                    iscriviTeamAHackathon(scanner);
                    break;
                case "7":
                    inviaSottomissione(scanner);
                    break;
                case "8":
                    valutaSottomissione(scanner);
                    break;
                case "9":
                    apriRichiestaSupporto(scanner);
                    break;
                case "10":
                    visualizzaRichiesteSupporto();
                    break;
                case "11":
                    proclamaVincitore(scanner);
                    break;
                case "0":
                    System.out.println("Chiusura HackHub CLI.");
                    return;
                default:
                    System.out.println("Scelta non valida.");
            }
        }
    }

    private void visualizzaUtenti() {
        System.out.println("\n--- Utenti ---");
        utenteRepository.findAll().forEach(u ->
                System.out.println(u.getId() + " - " + u.getNome() + " - " + u.getEmail())
        );
    }

    private void visualizzaTeam() {
        System.out.println("\n--- Team ---");
        teamRepository.findAll().forEach(t ->
                System.out.println(t.getId() + " - " + t.getNomeTeam() + " - membri: " + t.getDimensioneTeam())
        );
    }

    private void visualizzaHackathon() {
        System.out.println("\n--- Hackathon ---");
        hackathonRepository.findAll().forEach(h -> {
            String vincitore = h.getTeamVincitore() == null ? "nessuno" : h.getTeamVincitore().getNomeTeam();

            System.out.println(h.getId() + " - " + h.getNome()
                    + " - stato: " + h.getStato()
                    + " - iscrizioni: " + h.getIscrizioni().size()
                    + " - sottomissioni: " + h.getSottomissioni().size()
                    + " - vincitore: " + vincitore);
        });
    }

    private void visualizzaSottomissioni() {
        System.out.println("\n--- Sottomissioni ---");

        if (sottomissioneRepository.findAll().isEmpty()) {
            System.out.println("Nessuna sottomissione presente.");
            return;
        }

        sottomissioneRepository.findAll().forEach(s ->
                System.out.println(s.getId()
                        + " - Team ID: " + s.getTeamId()
                        + " - Hackathon ID: " + s.getHackathonId()
                        + " - Contenuto: " + s.getContenuto()
                        + " - Valutata: " + s.risultaValutata()
                        + " - Media: " + s.getPunteggioMedio())
        );
    }

    private void creaTeam(Scanner scanner) {
        try {
            System.out.print("Nome del nuovo team: ");
            String nomeTeam = scanner.nextLine();

            if (nomeTeam == null || nomeTeam.isBlank()) {
                System.out.println("Errore: il nome del team non può essere vuoto.");
                return;
            }

            Long nuovoId = generaNuovoTeamId();
            Team nuovoTeam = new Team(nuovoId, nomeTeam);

            teamRepository.save(nuovoTeam);

            System.out.println("Team creato con successo: " + nuovoTeam.getId() + " - " + nuovoTeam.getNomeTeam());
        } catch (Exception e) {
            System.out.println("Errore creazione team: " + e.getMessage());
        }
    }

    private void iscriviTeamAHackathon(Scanner scanner) {
        try {
            visualizzaTeam();
            Long teamId = leggiLong(scanner, "ID team da iscrivere: ");

            visualizzaHackathon();
            Long hackathonId = leggiLong(scanner, "ID hackathon: ");

            Optional<Team> teamOpt = trovaTeam(teamId);
            Optional<Hackathon> hackathonOpt = trovaHackathon(hackathonId);

            if (teamOpt.isEmpty()) {
                System.out.println("Errore: team inesistente.");
                return;
            }

            if (hackathonOpt.isEmpty()) {
                System.out.println("Errore: hackathon inesistente.");
                return;
            }

            Team team = teamOpt.get();
            Hackathon hackathon = hackathonOpt.get();

            hackathon.iscriviTeam(team);
            hackathonRepository.save(hackathon);

            System.out.println("Team iscritto con successo all'hackathon.");
        } catch (Exception e) {
            System.out.println("Errore iscrizione: " + e.getMessage());
        }
    }

    private void inviaSottomissione(Scanner scanner) {
        try {
            visualizzaTeam();
            Long teamId = leggiLong(scanner, "ID team: ");

            visualizzaHackathon();
            Long hackathonId = leggiLong(scanner, "ID hackathon: ");

            System.out.print("Contenuto/link sottomissione: ");
            String contenuto = scanner.nextLine();

            Optional<Team> teamOpt = trovaTeam(teamId);
            Optional<Hackathon> hackathonOpt = trovaHackathon(hackathonId);

            if (teamOpt.isEmpty()) {
                System.out.println("Errore: team inesistente.");
                return;
            }

            if (hackathonOpt.isEmpty()) {
                System.out.println("Errore: hackathon inesistente.");
                return;
            }

            Team team = teamOpt.get();
            Hackathon hackathon = hackathonOpt.get();

            if (!hackathon.verificaGiaIscritto(team.getId())) {
                System.out.println("Errore: il team non è iscritto all'hackathon.");
                return;
            }

            team.registraNuovaSottomissione(contenuto);

            Sottomissione sottomissione = new Sottomissione(
                    generaNuovoSottomissioneId(),
                    hackathon.getId(),
                    team.getId(),
                    contenuto
            );

            hackathon.associaSottomissione(sottomissione);

            sottomissioneRepository.save(sottomissione);
            hackathonRepository.save(hackathon);

            System.out.println("Sottomissione inviata con successo.");
        } catch (Exception e) {
            System.out.println("Errore sottomissione: " + e.getMessage());
        }
    }

    private void valutaSottomissione(Scanner scanner) {
        try {
            visualizzaSottomissioni();

            Long sottomissioneId = leggiLong(scanner, "ID sottomissione da valutare: ");
            Long giudiceId = leggiLong(scanner, "ID giudice: ");
            int punteggio = leggiInt(scanner, "Punteggio 0-10: ");

            System.out.print("Commento: ");
            String commento = scanner.nextLine();

            Optional<Sottomissione> sottomissioneOpt = trovaSottomissione(sottomissioneId);

            if (sottomissioneOpt.isEmpty()) {
                System.out.println("Errore: sottomissione inesistente.");
                return;
            }

            Sottomissione sottomissione = sottomissioneOpt.get();

            Valutazione valutazione = sottomissione.aggiornaValutazione(
                    giudiceId,
                    punteggio,
                    commento
            );

            sottomissioneRepository.save(sottomissione);

            System.out.println("Valutazione registrata con successo.");
            System.out.println("ID valutazione: " + valutazione.getId()
                    + " - punteggio: " + valutazione.getPunteggio());
        } catch (Exception e) {
            System.out.println("Errore valutazione: " + e.getMessage());
        }
    }

    private void apriRichiestaSupporto(Scanner scanner) {
        try {
            Long utenteId = leggiLong(scanner, "ID utente richiedente: ");
            Long teamId = leggiLong(scanner, "ID team: ");
            Long hackathonId = leggiLong(scanner, "ID hackathon: ");

            System.out.print("Topic: ");
            String topic = scanner.nextLine();

            System.out.print("Descrizione: ");
            String descrizione = scanner.nextLine();

            System.out.print("Priorità [BASSA/MEDIA/ALTA]: ");
            String priorita = scanner.nextLine();

            RichiestaSupporto richiesta = new RichiestaSupporto(
                    generaNuovoRichiestaId(),
                    utenteId,
                    teamId,
                    hackathonId,
                    topic,
                    descrizione,
                    priorita
            );

            richiesteSupporto.add(richiesta);

            System.out.println("Richiesta supporto aperta con successo.");
        } catch (Exception e) {
            System.out.println("Errore richiesta supporto: " + e.getMessage());
        }
    }

    private void visualizzaRichiesteSupporto() {
        System.out.println("\n--- Richieste Supporto ---");

        if (richiesteSupporto.isEmpty()) {
            System.out.println("Nessuna richiesta presente.");
            return;
        }

        for (RichiestaSupporto r : richiesteSupporto) {
            System.out.println(r.getId()
                    + " - Team ID: " + r.getTeamId()
                    + " - Hackathon ID: " + r.getHackathonId()
                    + " - Topic: " + r.getTopic()
                    + " - Priorità: " + r.getPriorita()
                    + " - Aperta: " + r.isAperta());
        }
    }

    private void proclamaVincitore(Scanner scanner) {
        try {
            visualizzaHackathon();
            Long hackathonId = leggiLong(scanner, "ID hackathon: ");

            Optional<Hackathon> hackathonOpt = trovaHackathon(hackathonId);

            if (hackathonOpt.isEmpty()) {
                System.out.println("Errore: hackathon inesistente.");
                return;
            }

            Hackathon hackathon = hackathonOpt.get();

            Optional<Sottomissione> migliore = hackathon.getSottomissioni()
                    .stream()
                    .filter(Sottomissione::risultaValutata)
                    .max(Comparator.comparingDouble(Sottomissione::getPunteggioMedio));

            if (migliore.isEmpty()) {
                System.out.println("Errore: nessuna sottomissione valutata.");
                return;
            }

            Optional<Team> teamVincitoreOpt = trovaTeam(migliore.get().getTeamId());

            if (teamVincitoreOpt.isEmpty()) {
                System.out.println("Errore: team vincitore non trovato.");
                return;
            }

            Team vincitore = teamVincitoreOpt.get();

            hackathon.setVincitore(vincitore);
            hackathon.setStato(HackathonStatus.CONCLUSO);
            hackathonRepository.save(hackathon);

            System.out.println("Vincitore proclamato: " + vincitore.getNomeTeam()
                    + " con media " + migliore.get().getPunteggioMedio());
        } catch (Exception e) {
            System.out.println("Errore proclamazione vincitore: " + e.getMessage());
        }
    }

    private void caricaDatiDimostrativi() {
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

        Sottomissione sottomissione = new Sottomissione(
                1L,
                hackathon.getId(),
                team.getId(),
                "https://repo.demo/team-alpha"
        );

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

    private Optional<Team> trovaTeam(Long id) {
        return teamRepository.findAll()
                .stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    private Optional<Hackathon> trovaHackathon(Long id) {
        return hackathonRepository.findAll()
                .stream()
                .filter(h -> h.getId().equals(id))
                .findFirst();
    }

    private Optional<Sottomissione> trovaSottomissione(Long id) {
        return sottomissioneRepository.findAll()
                .stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    private Long generaNuovoTeamId() {
        return teamRepository.findAll()
                .stream()
                .map(Team::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }

    private Long generaNuovoSottomissioneId() {
        return sottomissioneRepository.findAll()
                .stream()
                .map(Sottomissione::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }

    private Long generaNuovoRichiestaId() {
        return richiesteSupporto.stream()
                .map(RichiestaSupporto::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }

    private Long leggiLong(Scanner scanner, String messaggio) {
        System.out.print(messaggio);
        return Long.parseLong(scanner.nextLine());
    }

    private int leggiInt(Scanner scanner, String messaggio) {
        System.out.print(messaggio);
        return Integer.parseInt(scanner.nextLine());
    }
}