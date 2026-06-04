package it.hackhub.domain;

public class Valutazione {
    private Long id;
    private Long hackathonId;
    private Long teamId;
    private Long sottomissioneId;
    private Long giudiceId;
    private int punteggio;
    private String commento;

    public Valutazione(Long hackathonId, Long teamId, int punteggio, String commento) {
        this(System.currentTimeMillis(), hackathonId, teamId, null, null, punteggio, commento);
    }

    public Valutazione(Long id, Long hackathonId, Long teamId, Long sottomissioneId,
                       Long giudiceId, int punteggio, String commento) {
        if (punteggio < 0 || punteggio > 10) {
            throw new IllegalArgumentException("Il punteggio deve essere compreso tra 0 e 10");
        }
        this.id = id;
        this.hackathonId = hackathonId;
        this.teamId = teamId;
        this.sottomissioneId = sottomissioneId;
        this.giudiceId = giudiceId;
        this.punteggio = punteggio;
        this.commento = commento;
    }

    public Long getId() { return id; }
    public Long getHackathonId() { return hackathonId; }
    public Long getTeamId() { return teamId; }
    public Long getSottomissioneId() { return sottomissioneId; }
    public Long getGiudiceId() { return giudiceId; }
    public int getPunteggio() { return punteggio; }
    public String getCommento() { return commento; }
}
