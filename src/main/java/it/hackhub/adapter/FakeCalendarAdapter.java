package it.hackhub.adapter;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class FakeCalendarAdapter implements CalendarAdapter {
    public String pianificaCall(String titolo, LocalDateTime dataOra) {
        return "https://calendar.hackhub.local/call/" + Math.abs((titolo + dataOra).hashCode());
    }
}
