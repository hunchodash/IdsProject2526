package it.hackhub.domain;

public class Valutazione {
    private Long hackathonId;
    private Long teamId;
    private int punteggio; // Da 0 a 10
    private String commento;

    public Valutazione(Long hackathonId, Long teamId, int punteggio, String commento) {
        this.hackathonId = hackathonId;
        this.teamId = teamId;
        this.punteggio = punteggio;
        this.commento = commento;
    }

    public Long getTeamId() { return teamId; }
    public int getPunteggio() { return punteggio; }
    public Long getHackathonId() { return hackathonId; }
}