# HackHub – Iterazione Finale

## Descrizione

HackHub è una piattaforma per la gestione di hackathon che supporta l'intero ciclo di vita di una competizione, dalla creazione dell'evento fino alla proclamazione del team vincitore.

La piattaforma consente la collaborazione tra organizzatori, giudici, mentori e partecipanti, offrendo strumenti per la gestione degli hackathon, delle iscrizioni, delle sottomissioni, delle valutazioni e delle attività di supporto.

Questa versione rappresenta la release finale del progetto e integra tutte le funzionalità sviluppate nel corso delle iterazioni precedenti.

---

## Attori del Sistema

### Visitatore
- Consulta gli hackathon pubblici.
- Può registrarsi alla piattaforma.

### Utente
- Gestisce il proprio profilo.
- Può creare un team.
- Può accettare o rifiutare inviti ai team.

### Membro del Team
- Iscrive il proprio team agli hackathon.
- Invia e aggiorna le sottomissioni.
- Richiede supporto ai mentori.

### Organizzatore
- Crea e gestisce gli hackathon.
- Assegna giudici e mentori.
- Proclama il team vincitore.

### Giudice
- Visualizza le sottomissioni degli hackathon assegnati.
- Valuta le sottomissioni tramite punteggio e commento.

### Mentore
- Gestisce le richieste di supporto.
- Propone call di assistenza.
- Segnala eventuali violazioni del regolamento.

---

## Funzionalità Implementate

### Gestione Utenti
- Registrazione e autenticazione.
- Gestione del profilo personale.
- Invio e gestione degli inviti ai team.

### Gestione Team
- Creazione dei team.
- Accettazione e rifiuto degli inviti.
- Controllo del vincolo di appartenenza ad un solo team.

### Gestione Hackathon
- Creazione di nuovi hackathon.
- Gestione delle informazioni dell'evento.
- Assegnazione di giudici e mentori.
- Consultazione degli hackathon disponibili.

### Gestione Sottomissioni
- Invio delle sottomissioni.
- Aggiornamento delle sottomissioni entro la scadenza.
- Consultazione delle sottomissioni del team.

### Gestione Valutazioni
- Valutazione delle sottomissioni.
- Inserimento di commenti e punteggi.
- Calcolo dei risultati finali.

### Gestione Supporto
- Apertura di richieste di supporto.
- Visualizzazione delle richieste da parte dei mentori.
- Pianificazione di call tramite sistema esterno.

### Gestione Vincitore
- Selezione del team vincitore.
- Erogazione simulata del premio.
- Chiusura dell'hackathon.

---

## Architettura

Il progetto adotta un'architettura multilivello composta dai seguenti componenti:

- Domain Model
- Repository
- Service
- Controller
- CLI
- Adapter

La logica applicativa è separata dall'interfaccia utente tramite controller e servizi dedicati.

La persistenza è realizzata tramite repository in memoria.

---

## Design Pattern Utilizzati

### Strategy
Utilizzato per la selezione del team vincitore dell'hackathon.

### Adapter
Utilizzato per integrare servizi esterni simulati:

- Sistema di pagamento
- Sistema di calendarizzazione delle call

---

## Tecnologie Utilizzate

- Java 17
- Spring Boot 3
- Maven

---

## Avvio del Progetto

Eseguire la classe:

```java
HackHubApplication
```

oppure:

```bash
mvn spring-boot:run
```

---

## Principali Use Case

### Utente
- Registrarsi.
- Effettuare il login.
- Gestire il profilo.
- Creare un team.

### Membro del Team
- Iscrivere il team ad un hackathon.
- Inviare una sottomissione.
- Aggiornare una sottomissione.
- Richiedere supporto.

### Organizzatore
- Creare un hackathon.
- Assegnare giudici e mentori.
- Proclamare il vincitore.

### Giudice
- Consultare le sottomissioni.
- Valutare le sottomissioni.

### Mentore
- Visualizzare richieste di supporto.
- Proporre call di supporto.
- Segnalare team all'organizzatore.

---

## Evoluzione del Progetto

Nel corso delle quattro iterazioni il progetto è stato progressivamente esteso passando da una semplice gestione degli hackathon ad una piattaforma completa che supporta:

- Gestione utenti e autenticazione.
- Gestione team e inviti.
- Gestione del personale di supporto.
- Workflow completo delle sottomissioni.
- Sistema di valutazione.
- Gestione delle richieste di supporto.
- Integrazione con servizi esterni.
- Proclamazione del vincitore ed erogazione del premio.

---

## Stato del Progetto

Versione finale sviluppata per il corso di Ingegneria del Software.
