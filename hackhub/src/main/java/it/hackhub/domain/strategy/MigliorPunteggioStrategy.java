package it.hackhub.domain.strategy;

import it.hackhub.domain.Valutazione;
import java.util.Comparator;
import java.util.List;

public class MigliorPunteggioStrategy implements StrategiaVittoria {
    @Override
    public Long calcolaVincitore(List<Valutazione> valutazioni) {
        if (valutazioni.isEmpty()) return null;

        return valutazioni.stream()
                .max(Comparator.comparingInt(Valutazione::getPunteggio))
                .map(Valutazione::getTeamId)
                .orElse(null);
    }
}