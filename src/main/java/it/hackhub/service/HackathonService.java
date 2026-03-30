package it.hackhub.service;

import it.hackhub.domain.Hackathon;
import it.hackhub.domain.Iscrizione;
import it.hackhub.domain.staff.Giudice;
import it.hackhub.domain.staff.Mentore;
import it.hackhub.domain.staff.Organizzatore;
import it.hackhub.domain.state.IscrizioneAperta;
import it.hackhub.domain.state.StatoHackathon;
import it.hackhub.repository.HackathonRepository;
import it.hackhub.repository.IscrizioneRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HackathonService {
    private final HackathonRepository hackathonRepository;
    private final IscrizioneRepository iscrizioneRepository;

    public HackathonService(HackathonRepository hackathonRepository, IscrizioneRepository iscrizioneRepository) {
        this.hackathonRepository = hackathonRepository;
        this.iscrizioneRepository = iscrizioneRepository;
    }

    public void creaHackathon(String nome, int maxSize, Organizzatore organizzatore, Giudice giudice) {
        Long nuovoId = System.currentTimeMillis();
        Hackathon nuovo = new Hackathon(nuovoId, nome, maxSize, new IscrizioneAperta(), organizzatore, giudice);
        hackathonRepository.save(nuovo);
    }

    public void aggiungiMentore(Long hackathonId, Mentore mentore) {
        Optional<Hackathon> hOpt = hackathonRepository.findById(hackathonId);
        if (hOpt.isPresent()) {
            Hackathon h = hOpt.get();
            h.aggiungiMentore(mentore);
            hackathonRepository.save(h);
        }
    }

    public List<Hackathon> getAllHackathon() {
        return hackathonRepository.findAll();
    }

    public Optional<Hackathon> getHackathonById(Long id) {
        return hackathonRepository.findById(id);
    }

    public List<Hackathon> getHackathonByStato(StatoHackathon stato) {
        return hackathonRepository.findByStato(stato);
    }

    public List<Iscrizione> getIscrizioniHackathon(Long hackathonId) {
        return iscrizioneRepository.findByHackathonId(hackathonId);
    }

    public void cambiaStato(Long hackathonId, StatoHackathon nuovoStato) {
        Hackathon h = hackathonRepository.findById(hackathonId)
                .orElseThrow(() -> new RuntimeException("Hackathon non trovato"));
        h.setStato(nuovoStato);
        hackathonRepository.save(h);
    }

    public void salvaHackathon(Hackathon hackathon) {
        hackathonRepository.save(hackathon);
    }
}