package it.hackhub.domain.state;

public interface StatoHackathon {
    String getNome();
    default boolean puoEffettuareIscrizione() { return false; }
    default boolean puoInviareSottomissione() { return false; }


    default boolean puoValutare() { return false; }
}