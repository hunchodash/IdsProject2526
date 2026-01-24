package it.hackhub.domain.state;

public interface StatoHackathon {
    String getNome();
    // Metodo fondamentale: di base nessuno stato permette l'iscrizione
    default boolean puoEffettuareIscrizione() { return false; }
}