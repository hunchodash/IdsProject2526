# HackHub

Piattaforma per la gestione di hackathon sviluppata in Java e Spring Boot.

HackHub supporta l'intero ciclo di vita di un hackathon: dalla creazione dell'evento fino alla valutazione delle sottomissioni e alla proclamazione del team vincitore. La piattaforma gestisce utenti, team, organizzatori, giudici e mentori, ciascuno con ruoli e responsabilità differenti. L'interfaccia è una CLI role-aware.

---

## Indice

- [Ciclo di vita dell'Hackathon](#ciclo-di-vita-dellhackathon)
- [Attori del sistema](#attori-del-sistema)
- [Funzionalità implementate](#funzionalità-implementate)
- [Architettura](#architettura)
- [Design Pattern](#design-pattern)
- [Tecnologie](#tecnologie)
- [Avvio del progetto](#avvio-del-progetto)
- [Dati dimostrativi](#dati-dimostrativi)
- [Flusso di test consigliato](#flusso-di-test-consigliato)

---

## Ciclo di vita dell'Hackathon

Ogni hackathon attraversa quattro stati principali:

| Stato               | Operazioni disponibili                                           |
|---------------------|------------------------------------------------------------------|
| `ISCRIZIONI_APERTE` | Iscrizione dei team                                             |
| `IN_CORSO`          | Invio e aggiornamento sottomissioni, richieste di supporto      |
| `VALUTAZIONE`       | Valutazione delle sottomissioni da parte del giudice            |
| `CONCLUSO`          | Consultazione del vincitore proclamato                          |

Le date vengono validate rispettando la sequenza logica:

```
scadenza iscrizioni <= data inizio < data fine
```

---

## Attori del sistema

### Visitatore
Utente non autenticato. Può consultare gli hackathon pubblici, registrarsi e accedere. Non può accedere alle funzionalità riservate.

### Utente registrato
Può gestire il proprio profilo, creare un team, ricevere e gestire inviti. Un utente può appartenere a **un solo team alla volta**.

### Membro del Team
Può iscrivere il team a un hackathon, inviare e aggiornare sottomissioni, e inviare richieste di supporto ai mentori.

### Organizzatore
Membro dello staff. Può creare hackathon, assegnare giudici e mentori, consultare le sottomissioni, proclamare il vincitore e attivare l'erogazione simulata del premio. La creazione di un hackathon richiede **almeno un giudice e un mentore**.

### Giudice
Membro dello staff. Può visualizzare le sottomissioni degli hackathon assegnati e valutarle con un punteggio (0–10) e un commento testuale.

### Mentore
Membro dello staff. Può visualizzare le richieste di supporto, proporre call tramite il sistema Calendar simulato e segnalare violazioni del regolamento all'organizzatore.

---

## Funzionalità implementate

- **Utenti:** registrazione, login, gestione profilo, distinzione ruoli, CLI differenziata per ruolo
- **Team:** creazione, inviti, accettazione/rifiuto, vincolo di appartenenza singola, iscrizione agli hackathon
- **Hackathon:** creazione, configurazione (nome, regolamento, date, luogo, premio, dimensione team), assegnazione staff, consultazione pubblica
- **Sottomissioni:** invio, aggiornamento, controllo iscrizione e stato hackathon; ogni team può avere **una sola sottomissione per hackathon**
- **Valutazioni:** punteggio 0–10, commento testuale, classifica finale
- **Supporto:** apertura richieste, visualizzazione da parte dei mentori, pianificazione call, segnalazione violazioni
- **Vincitore:** calcolo classifica, selezione automatica, proclamazione, chiusura hackathon, erogazione simulata del premio

---

## Architettura

Il progetto adotta un'architettura multilivello:

```
CLI
 ↓
Controller
 ↓
Service
 ↓
Repository
 ↓
Domain Model
```

| Package       | Responsabilità                                          |
|---------------|---------------------------------------------------------|
| `domain`      | Entità principali del dominio                           |
| `domain.staff`| Ruoli dello staff                                       |
| `controller`  | Operazioni esposte alla CLI                             |
| `service`     | Logica applicativa                                      |
| `repository`  | Accesso ai dati (in memoria)                            |
| `adapter`     | Integrazioni simulate con sistemi esterni               |
| `strategy`    | Logica di selezione del vincitore                       |
| `cli`         | Interfaccia a linea di comando e dati dimostrativi      |

La persistenza è realizzata **in memoria** tramite repository Java. I dati vengono persi alla chiusura dell'applicazione e ripristinati dai dati demo al riavvio. L'architettura a interfacce permette di sostituire i repository in memoria con implementazioni basate su database (es. Spring Data JPA) senza modificare i livelli superiori.

---

## Design Pattern

### Strategy Pattern — Selezione del vincitore

Separa il criterio di selezione dalla logica principale dell'hackathon, rendendo semplice introdurre nuovi criteri in futuro (es. voto del pubblico, criteri misti).

- `WinnerSelectionStrategy` — interfaccia
- `HighestScoreWinnerStrategy` — seleziona il team con il punteggio medio più alto

### Adapter Pattern — Servizi esterni simulati

Disaccoppia i servizi esterni dall'applicazione. Le implementazioni fake possono essere sostituite con integrazioni reali senza modificare il resto del sistema.

- `CalendarAdapter` / `FakeCalendarAdapter` — pianificazione call mentore-team
- `PaymentAdapter` / `FakePaymentAdapter` — erogazione simulata del premio

---

## Tecnologie

- Java 17
- Spring Boot 3
- Maven
- CLI come strato di presentazione
- Repository in memoria

---

## Avvio del progetto

**Compilazione:**
```bash
mvn clean compile
```

**Avvio:**
```bash
mvn spring-boot:run
```

In alternativa, eseguire direttamente la classe principale:
```java
HackHubApplication
```

---

## Dati dimostrativi

All'avvio vengono caricati automaticamente dati demo per testare il sistema.

### Utenti

| Ruolo          | Email                  | Password   |
|----------------|------------------------|------------|
| Utente normale | `mario@hackhub.it`     | `password` |
| Organizzatore  | `admin@hackhub.it`     | `password` |
| Giudice        | `giudice@hackhub.it`   | `password` |
| Mentore        | `mentore@hackhub.it`   | `password` |

### Dati precaricati

| Elemento              | Valore                              |
|-----------------------|-------------------------------------|
| Team                  | Team Alpha                          |
| Hackathon – Iscrizioni| HackHub Demo - Iscrizioni           |
| Hackathon – In Corso  | HackHub Demo - In Corso             |
| Hackathon – Valutazione| HackHub Demo - Valutazione         |
| Sottomissione demo    | `https://repo.demo/team-alpha`      |

---

## Flusso di test consigliato

1. Consultare gli hackathon come **visitatore**
2. Effettuare il login come **utente normale** e gestire il team
3. Inviare una **richiesta di supporto**
4. Effettuare il login come **giudice** e valutare una sottomissione
5. Effettuare il login come **organizzatore** e proclamare il vincitore
6. Effettuare il login come **mentore** e proporre una call di supporto


