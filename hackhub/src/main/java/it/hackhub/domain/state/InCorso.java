package it.hackhub.domain.state;

public class InCorso implements StatoHackathon {
    @Override
    public String getNome() {
        return "IN_CORSO";
    }

    @Override
    public boolean puoInviareSottomissione() {
        return true;
    }
}