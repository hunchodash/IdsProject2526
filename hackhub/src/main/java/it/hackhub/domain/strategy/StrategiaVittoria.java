package it.hackhub.domain.strategy;

import it.hackhub.domain.Valutazione;
import java.util.List;

public interface StrategiaVittoria {
    Long calcolaVincitore(List<Valutazione> valutazioni);
}