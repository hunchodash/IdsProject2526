package it.hackhub.domain;

public class Valutazione {
    private Long hackathonId;
    private Long teamId;
    private int punteggio;
    private String commento;

    public Valutazione(Long hackathonId, Long teamId, int punteggio, String commento) {

        if (punteggio < 0 || punteggio > 10) {
            throw new IllegalArgumentException("Il punteggio deve essere tra 0 e 10");
        }

        this.hackathonId = hackathonId;
        this.teamId = teamId;
        this.punteggio = punteggio;
        this.commento = commento;
    }


    public Long getHackathonId() { return hackathonId; }
    public Long getTeamId() { return teamId; }
    public int getPunteggio() { return punteggio; }
    public String getCommento() { return commento; }
}