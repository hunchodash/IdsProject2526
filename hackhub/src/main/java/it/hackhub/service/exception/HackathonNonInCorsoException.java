package it.hackhub.service.exception;

public class HackathonNonInCorsoException extends RuntimeException {
    public HackathonNonInCorsoException() {
        super("Impossibile inviare la sottomissione: l'hackathon non è in corso.");
    }
}