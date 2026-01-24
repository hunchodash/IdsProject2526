package it.hackhub.domain.state;

public class IscrizioneAperta implements StatoHackathon {
    @Override
    public String getNome() { return "ISCRIZIONE_APERTA"; }

    @Override
    public boolean puoEffettuareIscrizione() { return true; } // Solo questo stato ritorna true
}