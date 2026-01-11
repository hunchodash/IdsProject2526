package it.hackhub.domain;

public class Iscrizione {
    private Long hackathonId;
    private Long teamId;

    public Iscrizione(Long hackathonId, Long teamId) {
        this.hackathonId = hackathonId;
        this.teamId = teamId;
    }

    public Long getHackathonId() {
        return hackathonId;
    }

    public Long getTeamId() {
        return teamId;
    }
}
