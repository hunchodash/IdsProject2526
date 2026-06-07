package it.hackhub.adapter;

import java.time.LocalDateTime;

public interface CalendarAdapter {
    String pianificaCall(String titolo, LocalDateTime dataOra);
}
