

# HackHub - Iterazione 3

## Descrizione

HackHub è una piattaforma per la gestione di hackathon che consente l'interazione tra organizzatori, team, giudici e mentori durante l'intero ciclo di vita di una competizione.

Questa terza iterazione estende la seconda introducendo una struttura applicativa più completa, una migliore separazione delle responsabilità tra i componenti del sistema e nuovi casi d'uso relativi alla gestione degli utenti, dei profili e delle attività di supporto.



## Funzionalità Implementate

### Gestione Utenti

* Registrazione di nuovi utenti
* Autenticazione tramite login
* Visualizzazione del profilo
* Modifica delle informazioni del profilo

### Gestione Team

* Creazione di team
* Associazione del creatore come membro del team
* Iscrizione di un team a un hackathon
* Controllo dei vincoli di partecipazione

### Gestione Hackathon

* Creazione di hackathon
* Gestione dello stato dell'hackathon
* Consultazione degli hackathon disponibili
* Assegnazione di giudici e mentori
* Chiusura dell'hackathon e proclamazione del vincitore

### Gestione Sottomissioni

* Invio di una sottomissione
* Associazione della sottomissione all'hackathon
* Controllo delle condizioni di invio

### Gestione Valutazioni

* Valutazione delle sottomissioni da parte dei giudici
* Inserimento di punteggi e commenti
* Calcolo dei risultati finali
* Generazione della classifica

### Gestione Supporto

* Apertura di richieste di supporto
* Consultazione delle richieste aperte
* Visualizzazione delle richieste da parte dei mentori

### Gestione Vincitore

* Determinazione del team vincitore sulla base delle valutazioni
* Erogazione simulata del premio tramite adapter di pagamento
* Aggiornamento dello stato dell'hackathon

---

## Architettura

Il progetto segue una struttura multilivello composta da:

* Entity
* Repository
* Service
* Controller
* CLI
* Adapter

Le responsabilità applicative sono distribuite tra controller dedicati ai principali casi d'uso e servizi specializzati per la logica di business.

La persistenza continua ad essere realizzata tramite repository in memoria.

---

## Tecnologie Utilizzate

* Java 17
* Spring Boot 3
* Maven

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

## Principali Use Case Implementati

### Utente

* Registrarsi
* Accedere
* Gestire il proprio profilo
* Creare un team

### Membro Team

* Iscrivere un team a un hackathon
* Inviare una sottomissione
* Inviare una richiesta di supporto

### Organizzatore

* Creare un hackathon
* Definire le informazioni dell'hackathon
* Assegnare giudici e mentori
* Proclamare il vincitore

### Giudice

* Valutare una sottomissione

### Mentore

* Visualizzare le richieste di supporto

---

## Evoluzione Rispetto alla Seconda Iterazione

Le principali novità introdotte sono:

* Introduzione della gestione utenti e autenticazione.
* Gestione e modifica del profilo utente.
* Introduzione di controller dedicati ai principali casi d'uso.
* Maggiore separazione tra interfaccia utente e logica applicativa.
* Miglioramento della gestione delle richieste di supporto.
* Introduzione della classifica finale.
* Gestione della proclamazione del vincitore.
* Utilizzo di adapter per l'integrazione con servizi esterni simulati.
* Maggiore aderenza ai diagrammi UML di analisi, progetto e sequenza della terza iterazione.

---
