package it.hackhub.domain;

import java.time.LocalDateTime;

public class CallSupporto {
    private Long id;
    private Long richiestaSupportoId;
    private Long mentoreId;
    private LocalDateTime dataOra;
    private String link;

    public CallSupporto(Long id, Long richiestaSupportoId, Long mentoreId, LocalDateTime dataOra, String link) {
        if (dataOra == null) {
            throw new IllegalArgumentException("Data e ora della call obbligatorie");
        }
        this.id = id;
        this.richiestaSupportoId = richiestaSupportoId;
        this.mentoreId = mentoreId;
        this.dataOra = dataOra;
        this.link = link;
    }

    public Long getId() { return id; }
    public Long getRichiestaSupportoId() { return richiestaSupportoId; }
    public Long getMentoreId() { return mentoreId; }
    public LocalDateTime getDataOra() { return dataOra; }
    public String getLink() { return link; }
}
