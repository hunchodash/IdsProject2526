package it.hackhub.service.exception;

public class TeamNonIscrittoException extends RuntimeException {
    public TeamNonIscrittoException() { super("Il team non risulta iscritto all'hackathon"); }
}
