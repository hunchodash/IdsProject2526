package it.hackhub.service;

import it.hackhub.domain.Hackathon;
import it.hackhub.domain.Team;
import it.hackhub.domain.Utente;
import it.hackhub.repository.HackathonRepository;
import it.hackhub.repository.InvitoTeamRepository;
import it.hackhub.repository.TeamRepository;
import it.hackhub.repository.UtenteRepository;
import it.hackhub.domain.InvitoTeam;
import it.hackhub.service.exception.HackathonNonTrovatoException;
import it.hackhub.service.exception.TeamNonTrovatoException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final HackathonRepository hackathonRepository;
    private final UtenteRepository utenteRepository;
    private final InvitoTeamRepository invitoTeamRepository;

    public TeamService(TeamRepository teamRepository,
                       HackathonRepository hackathonRepository,
                       UtenteRepository utenteRepository,
                       InvitoTeamRepository invitoTeamRepository) {
        this.teamRepository = teamRepository;
        this.hackathonRepository = hackathonRepository;
        this.utenteRepository = utenteRepository;
        this.invitoTeamRepository = invitoTeamRepository;
    }

    public Team creaTeam(String nomeTeam, Utente creatore) {
        if (nomeTeam == null || nomeTeam.isBlank()) {
            throw new IllegalArgumentException("Nome team obbligatorio");
        }
        if (teamRepository.findByNomeTeam(nomeTeam).isPresent()) {
            throw new IllegalStateException("Nome team già esistente");
        }
        if (!teamRepository.findByMembro(creatore).isEmpty()) {
            throw new IllegalStateException("Un utente può appartenere a un solo team");
        }
        Team team = new Team(System.currentTimeMillis(), nomeTeam);
        team.aggiungiMembro(creatore);
        teamRepository.save(team);
        return team;
    }

    public void iscriviTeam(Long teamId, Long hackathonId) {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNonTrovatoException::new);
        Hackathon hackathon = hackathonRepository.findById(hackathonId).orElseThrow(HackathonNonTrovatoException::new);
        hackathon.iscriviTeam(team);
        hackathonRepository.save(hackathon);
    }

    public InvitoTeam invitaUtente(Long teamId, Long invitanteId, String emailInvitato) {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNonTrovatoException::new);
        Utente invitante = utenteRepository.findById(invitanteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente invitante non trovato"));
        if (!team.getMembri().contains(invitante)) {
            throw new IllegalStateException("Solo un membro del team può inviare inviti");
        }
        Utente invitato = utenteRepository.findByEmail(emailInvitato)
                .orElseThrow(() -> new IllegalArgumentException("Utente invitato non trovato"));
        if (!teamRepository.findByMembro(invitato).isEmpty()) {
            throw new IllegalStateException("L'utente invitato appartiene già a un team");
        }
        if (team.getMembri().contains(invitato)) {
            throw new IllegalStateException("Utente già presente nel team");
        }
        boolean invitoGiaPresente = invitoTeamRepository.findByUtenteInvitatoId(invitato.getId()).stream()
                .anyMatch(i -> i.isInAttesa() && i.getTeamId().equals(teamId));
        if (invitoGiaPresente) {
            throw new IllegalStateException("Invito già inviato");
        }
        InvitoTeam invito = new InvitoTeam(System.currentTimeMillis(), teamId, invitato.getId(), invitanteId);
        invitoTeamRepository.save(invito);
        return invito;
    }

    public void accettaInvito(Long invitoId, Long utenteId) {
        InvitoTeam invito = invitoTeamRepository.findById(invitoId)
                .orElseThrow(() -> new IllegalArgumentException("Invito non trovato"));
        if (!invito.getUtenteInvitatoId().equals(utenteId)) {
            throw new IllegalStateException("L'invito non appartiene all'utente indicato");
        }
        Team team = teamRepository.findById(invito.getTeamId()).orElseThrow(TeamNonTrovatoException::new);
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        if (!teamRepository.findByMembro(utente).isEmpty()) {
            throw new IllegalStateException("Un utente può appartenere a un solo team");
        }
        invito.accetta();
        team.aggiungiMembro(utente);
        invitoTeamRepository.save(invito);
        teamRepository.save(team);
    }

    public void rifiutaInvito(Long invitoId, Long utenteId) {
        InvitoTeam invito = invitoTeamRepository.findById(invitoId)
                .orElseThrow(() -> new IllegalArgumentException("Invito non trovato"));
        if (!invito.getUtenteInvitatoId().equals(utenteId)) {
            throw new IllegalStateException("L'invito non appartiene all'utente indicato");
        }
        invito.rifiuta();
        invitoTeamRepository.save(invito);
    }

    public List<InvitoTeam> getInvitiUtente(Long utenteId) {
        return invitoTeamRepository.findByUtenteInvitatoId(utenteId);
    }

    public List<Team> getTeamDiUtente(Utente utente) {
        return teamRepository.findByMembro(utente);
    }

    public List<Team> getTuttiITeam() { return teamRepository.findAll(); }
}
