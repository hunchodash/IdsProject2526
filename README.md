# HackHub - Iterazione 2

## Descrizione

HackHub è una piattaforma per la gestione di hackathon che permette agli utenti di creare team, partecipare a competizioni, inviare progetti e collaborare con giudici e mentori.

Questa seconda iterazione estende le funzionalità sviluppate nella prima versione introducendo la gestione delle iscrizioni agli hackathon, delle sottomissioni, delle valutazioni e delle richieste di supporto.

## Funzionalità implementate

- Creazione e gestione degli hackathon
- Creazione dei team
- Iscrizione dei team agli hackathon
- Invio delle sottomissioni
- Valutazione delle sottomissioni
- Gestione delle richieste di supporto
- Assegnazione di giudici e mentori agli hackathon
- Proclamazione del team vincitore
- CLI interattiva per il test delle principali funzionalità

## Struttura del progetto

Il progetto è organizzato nei seguenti livelli:

- **Domain**: entità del dominio applicativo
- **Repository**: persistenza in memoria dei dati
- **Service**: logica applicativa
- **Controller**: gestione dei casi d'uso
- **Adapter**: simulazione di servizi esterni
- **CLI**: interfaccia testuale per il collaudo del sistema

## Tecnologie utilizzate

- Java
- Spring Boot
- Maven

## Esecuzione

Per avviare l'applicazione è sufficiente eseguire la classe:

`HackHubApplication`

All'avvio viene caricata una configurazione dimostrativa ed è disponibile una Command Line Interface (CLI) che consente di testare le funzionalità implementate.

## Stato del progetto

Questa iterazione rappresenta l'evoluzione della prima versione del sistema e implementa i casi d'uso previsti per la seconda iterazione del progetto di Ingegneria del Software.
