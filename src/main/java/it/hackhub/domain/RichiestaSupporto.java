package it.hackhub.domain;

public class RichiestaSupporto {
    private Long id;
    private Long utenteId;
    private String topic;
    private String descrizione;

    public RichiestaSupporto(Long id, Long utenteId, String topic, String descrizione) {
        if (topic == null || topic.isBlank() || descrizione == null || descrizione.isBlank()) {
            throw new IllegalArgumentException("Topic e descrizione sono obbligatori");
        }
        this.id = id;
        this.utenteId = utenteId;
        this.topic = topic;
        this.descrizione = descrizione;
    }

    public void salvaNelSistema() {
        // Metodo presente nel diagramma: il salvataggio reale è delegato al repository in memoria.
    }

    public Long getId() { return id; }
    public Long getUtenteId() { return utenteId; }
    public String getTopic() { return topic; }
    public String getDescrizione() { return descrizione; }
}
