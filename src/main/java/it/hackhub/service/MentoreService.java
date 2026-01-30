package it.hackhub.service;

import it.hackhub.adapter.CalendarAdapter;
import it.hackhub.domain.staff.Mentore;
import org.springframework.stereotype.Service;

@Service

public class MentoreService {
    private final CalendarAdapter calendarAdapter;


    public MentoreService(CalendarAdapter calendarAdapter) {
        this.calendarAdapter = calendarAdapter;
    }

    public String proponiCall(Mentore mentore, Long teamId) {
        String linkCall = calendarAdapter.prenotaCall(mentore.getEmail(), teamId);
        return linkCall;
    }
}