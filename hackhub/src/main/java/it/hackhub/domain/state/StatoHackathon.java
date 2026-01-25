package it.hackhub.domain.state;

public interface StatoHackathon {
    String getNome();
    default boolean puoEffettuareIscrizione() { return false; }
    default boolean puoInviareSottomissione() { return false; }

    // Nuovo metodo per la fase di valutazione
    default boolean puoValutare() { return false; }
}