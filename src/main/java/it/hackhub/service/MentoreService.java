package it.hackhub.service;

import it.hackhub.adapter.CalendarAdapter;
import it.hackhub.domain.RichiestaSupporto;
import it.hackhub.domain.staff.Mentore;
import it.hackhub.repository.RichiestaSupportoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MentoreService {
    private final CalendarAdapter calendarAdapter;
    private final RichiestaSupportoRepository richiestaSupportoRepository;

    public MentoreService(CalendarAdapter calendarAdapter, RichiestaSupportoRepository richiestaSupportoRepository) {
        this.calendarAdapter = calendarAdapter;
        this.richiestaSupportoRepository = richiestaSupportoRepository;
    }

    public void creaRichiestaSupporto(Long teamId, String nomeTeam, Mentore mentore) {
        Long nuovoId = System.currentTimeMillis();
        RichiestaSupporto richiesta = new RichiestaSupporto(nuovoId, teamId, nomeTeam, mentore);
        richiestaSupportoRepository.save(richiesta);
    }

    public List<RichiestaSupporto> getRichiestePendenti(Long mentoreId) {
        return richiestaSupportoRepository.findByMentoreIdAndGestita(mentoreId, false);
    }

    // Il mentore accetta la richiesta specifica e genera la call
    public String accettaRichiestaEPrenotaCall(Long richiestaId) {
        RichiestaSupporto richiesta = richiestaSupportoRepository.findById(richiestaId)
                .orElseThrow(() -> new RuntimeException("Richiesta di supporto non trovata"));

        if (richiesta.isGestita()) {
            throw new RuntimeException("Questa richiesta è già stata gestita");
        }

        String linkCall = calendarAdapter.prenotaCall(richiesta.getMentoreAssegnato().getEmail(), richiesta.getTeamId());

        richiesta.setGestita(true);
        richiestaSupportoRepository.save(richiesta);

        return linkCall;
    }

    public String proponiCall(Mentore mentore, Long teamId) {
        return calendarAdapter.prenotaCall(mentore.getEmail(), teamId);
    }
}