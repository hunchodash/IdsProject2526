package it.hackhub.domain;

import java.time.LocalDateTime;

public class RichiestaSupporto {
    private Long id;
    private Long utenteId;
    private Long teamId;
    private Long hackathonId;
    private String topic;
    private String descrizione;
    private LocalDateTime dataInvio;
    private String priorita;
    private boolean aperta;

    public RichiestaSupporto(Long id, Long utenteId, String topic, String descrizione) {
        this(id, utenteId, null, null, topic, descrizione, "MEDIA");
    }

    public RichiestaSupporto(Long id, Long utenteId, Long teamId, Long hackathonId,
                             String topic, String descrizione, String priorita) {
        if (topic == null || topic.isBlank() || descrizione == null || descrizione.isBlank()) {
            throw new IllegalArgumentException("Topic e descrizione sono obbligatori");
        }
        this.id = id;
        this.utenteId = utenteId;
        this.teamId = teamId;
        this.hackathonId = hackathonId;
        this.topic = topic;
        this.descrizione = descrizione;
        this.dataInvio = LocalDateTime.now();
        this.priorita = priorita == null || priorita.isBlank() ? "MEDIA" : priorita;
        this.aperta = true;
    }

    public void chiudi() { this.aperta = false; }

    public void salvaNelSistema() {
        // Metodo presente nel diagramma: il salvataggio reale è delegato al repository in memoria.
    }

    public Long getId() { return id; }
    public Long getUtenteId() { return utenteId; }
    public Long getTeamId() { return teamId; }
    public Long getHackathonId() { return hackathonId; }
    public String getTopic() { return topic; }
    public String getDescrizione() { return descrizione; }
    public LocalDateTime getDataInvio() { return dataInvio; }
    public String getPriorita() { return priorita; }
    public boolean isAperta() { return aperta; }
}
