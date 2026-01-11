package it.hackhub.service.exception;

public class HackathonNonTrovatoException extends RuntimeException {
    public HackathonNonTrovatoException() {
        super("Hackathon non trovato");
    }
}
