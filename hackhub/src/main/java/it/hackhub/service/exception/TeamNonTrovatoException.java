package it.hackhub.service.exception;

public class TeamNonTrovatoException extends RuntimeException {
    public TeamNonTrovatoException() {
        super("Team non trovato");
    }
}
