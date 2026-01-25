package it.hackhub.domain.state;

public class InValutazione implements StatoHackathon {
    @Override
    public String getNome() { return "IN_VALUTAZIONE"; }

    @Override
    public boolean puoValutare() { return true; }
}