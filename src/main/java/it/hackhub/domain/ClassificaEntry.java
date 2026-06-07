package it.hackhub.domain;

public class ClassificaEntry {
    private final Team team;
    private final Sottomissione sottomissione;
    private final double punteggioMedio;

    public ClassificaEntry(Team team, Sottomissione sottomissione, double punteggioMedio) {
        this.team = team;
        this.sottomissione = sottomissione;
        this.punteggioMedio = punteggioMedio;
    }

    public Team getTeam() { return team; }
    public Sottomissione getSottomissione() { return sottomissione; }
    public double getPunteggioMedio() { return punteggioMedio; }
}
