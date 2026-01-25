package it.hackhub.service.exception;

public class TeamGiaIscrittoException extends RuntimeException {
    public TeamGiaIscrittoException() {
        super("Team già iscritto all'hackathon");
    }
}
