package it.hackhub.service.exception;

public class HackathonNonInIscrizioneException extends RuntimeException {
    public HackathonNonInIscrizioneException() {
        super("Hackathon non in fase di iscrizione");
    }
}
