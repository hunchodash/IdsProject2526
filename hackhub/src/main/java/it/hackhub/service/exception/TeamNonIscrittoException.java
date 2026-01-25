package it.hackhub.service.exception;

public class TeamNonIscrittoException extends RuntimeException {
    public TeamNonIscrittoException() {
        super("Errore: Il team non risulta iscritto a questo hackathon.");
    }
}