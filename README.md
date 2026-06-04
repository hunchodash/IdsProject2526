\# HackHub - Iterazione 2



\## Descrizione



HackHub è una piattaforma per la gestione di hackathon che consente la creazione e l'organizzazione di eventi, la registrazione dei team, l'invio delle sottomissioni e la valutazione dei progetti.



Questa seconda iterazione estende le funzionalità della prima introducendo la gestione completa del ciclo di vita di un hackathon.



\---



\## Funzionalità implementate



\### Gestione Hackathon



\- Creazione hackathon

\- Gestione dello stato dell'hackathon

\- Assegnazione di giudici e mentori

\- Proclamazione del team vincitore



\### Gestione Team



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





