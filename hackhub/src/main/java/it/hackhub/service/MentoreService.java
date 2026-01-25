package it.hackhub.service;

import it.hackhub.adapter.CalendarAdapter;
import it.hackhub.domain.staff.Mentore;
import org.springframework.stereotype.Service;

@Service

public class MentoreService {
    private final CalendarAdapter calendarAdapter;

    // Costruttore deve ricevere l'adapter per il calendario
    public MentoreService(CalendarAdapter calendarAdapter) {
        this.calendarAdapter = calendarAdapter;
    }

    public String proponiCall(Mentore mentore, Long teamId) {
        // Usa l'adapter per simulare la creazione di un link su Google Meet/Zoom
        String linkCall = calendarAdapter.prenotaCall(mentore.getEmail(), teamId);
        return linkCall;
    }
}