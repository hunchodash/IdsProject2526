Hai ragione, ho esagerato con l'estremo opposto. Ecco una via di mezzo più sensata:

---

# HackHub

Applicazione Spring Boot per la gestione di hackathon.

## Requisiti

- Java 17+
- Maven

## Avvio

```bash
mvn spring-boot:run
```

All'avvio viene avviata un'interfaccia CLI interattiva.

## Dati di test preconfigurati

| Ruolo | Email | Password |
|-------|-------|----------|
| Organizzatore | aziz@hackhub.it | admin |
| Giudice | rossi@univ.it | judge |
| Mentore | piero@mentors.it | mentor |
| Utente | mario@email.it | 1234 |

È presente un hackathon "Java Challenge 2026" in stato ISCRIZIONE_APERTA e un team "Dev Team" con l'utente Mario come membro.

## Struttura del progetto

```
it.hackhub/
├── adapter/         # Interfacce per servizi esterni (pagamenti, calendario)
├── controller/      # REST controller (parziale)
├── domain/          # Entità e logica di business
│   ├── staff/       # Organizzatore, Giudice, Mentore
│   ├── state/       # Stati dell'hackathon (IscrizioneAperta, InCorso, ecc.)
│   └── strategy/    # Strategie per determinare il vincitore
├── repository/      # Interfacce per accesso dati
│   └── memory/      # Implementazioni in memoria (HashMap)
├── service/         # Servizi applicativi
│   └── exception/   # Eccezioni specifiche di dominio
└── HackHubApplication.java
```

## Pattern implementati

- **State**: gestione del ciclo di vita dell'hackathon
- **Strategy**: calcolo del vincitore intercambiabile
- **Adapter**: integrazione con servizi esterni
- **Repository**: astrazione dell'accesso ai dati

## Funzionalità principali

**Partecipanti**
- Registrazione e login
- Creazione team
- Iscrizione a hackathon
- Invio sottomissioni
- Richiesta supporto mentor
- Visualizzazione valutazioni

**Organizzatori**
- Creazione hackathon
- Proclamazione vincitore

**Giudici**
- Assegnazione punteggi (0-10) e commenti

**Mentor**
- Generazione link call (simulato)

## Stato attuale e limitazioni

- I dati sono persistenti solo in memoria (perduti allo spegnimento)
- Password in chiaro (nessun hashing)
- Autenticazione basata su controllo diretto email/password
- Nessun test automatizzato
- API REST solo abbozzate (presente solo TeamController)

## Possibili evoluzioni

- Sostituzione repository in-memory con JPA/PostgreSQL
- Spring Security con JWT e hashing password
- Esposizione API REST complete
- Test unitari e di integrazione
- Frontend separato (React/Angular)

## Note

Il progetto è pensato come base per comprendere l'architettura a strati e l'applicazione di pattern in un contesto Spring Boot. Le implementazioni attuali privilegiano la chiarezza rispetto alla completezza.
