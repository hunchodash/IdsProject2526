package it.hackhub.service.exception;

public class TeamTroppoGrandeException extends RuntimeException {
  public TeamTroppoGrandeException() {
    super("Errore: Il team supera la dimensione massima consentita per questo hackathon");
  }
}