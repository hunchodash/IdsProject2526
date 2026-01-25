package it.hackhub.adapter;
import org.springframework.stereotype.Component;

@Component

public class ExternalCalendarAdapter implements CalendarAdapter {

    @Override
    public String prenotaCall(String emailMentore, Long teamId) {
        System.out.println("DEBUG: Generazione slot per " + emailMentore);
        return "https://meet.google.com/call-team-" + teamId;
    }
}