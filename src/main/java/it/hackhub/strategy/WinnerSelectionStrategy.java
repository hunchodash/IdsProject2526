package it.hackhub.strategy;

import it.hackhub.domain.ClassificaEntry;
import java.util.List;

public interface WinnerSelectionStrategy {
    ClassificaEntry selezionaVincitore(List<ClassificaEntry> classifica);
}
