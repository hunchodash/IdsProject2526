HackHub - Iterazione 2
Descrizione

HackHub è una piattaforma per la gestione di hackathon che consente agli utenti di partecipare a competizioni, creare team, inviare progetti e collaborare con giudici e mentori.

Questa seconda iterazione amplia le funzionalità introdotte nella prima versione, aggiungendo la gestione delle iscrizioni agli hackathon, delle sottomissioni, delle valutazioni e delle richieste di supporto.

Funzionalità implementate
Creazione e gestione degli hackathon
Creazione dei team
Iscrizione dei team agli hackathon
Invio delle sottomissioni
Valutazione delle sottomissioni da parte dei giudici
Gestione delle richieste di supporto
Assegnazione di giudici e mentori agli hackathon
Proclamazione del team vincitore
Interfaccia a riga di comando (CLI) per il testing delle principali funzionalità
Architettura

Il progetto è organizzato secondo una struttura a livelli:

Domain: contiene le entità del dominio applicativo
Repository: gestione dei dati tramite repository in memoria
Service: implementazione della logica applicativa
Controller: coordinamento dei casi d'uso
Adapter: simulazione di servizi esterni
CLI: interfaccia testuale per l'esecuzione e il collaudo del sistema
Tecnologie utilizzate
Java
Spring Boot
Maven
Avvio del progetto

Per eseguire il progetto è sufficiente avviare la classe:

HackHubApplication

All'avvio viene caricata una configurazione dimostrativa e viene resa disponibile una CLI interattiva per testare le funzionalità implementate.

Iterazione

Questa iterazione implementa i casi d'uso previsti dalla seconda fase del progetto e rappresenta un'evoluzione della prima versione sviluppata durante il corso di Ingegneria del Software.



\- Creazione team

\- Iscrizione a un hackathon

\- Verifica dei vincoli di partecipazione



\### Gestione Sottomissioni



\- Invio di una sottomissione

\- Associazione della sottomissione all'hackathon



\### Gestione Valutazioni



\- Valutazione delle sottomissioni

\- Calcolo del punteggio medio



\### Gestione Supporto



\- Apertura di richieste di supporto

\- Consultazione delle richieste aperte



\### CLI Interattiva



La seconda iterazione include una Command Line Interface che permette di testare le principali funzionalità del sistema.



\---



\## Architettura



Il progetto segue una struttura a livelli:



\- Domain

\- Repository

\- Service

\- CLI



La persistenza è realizzata tramite repository in memoria.



\---



\## Tecnologie utilizzate



\- Java 11+

\- Spring Boot 3

\- Maven



\---



\## Avvio del progetto



Eseguire la classe:



```java

HackHubApplication

```



oppure:



```bash

mvn spring-boot:run

```



\---



\## Use Case implementati



\- Creare hackathon

\- Assegnare giudici e mentori

\- Iscrivere team a hackathon

\- Inviare una sottomissione

\- Valutare una sottomissione

\- Inviare una richiesta di supporto

\- Consultare richieste di supporto

\- Proclamare il vincitore





