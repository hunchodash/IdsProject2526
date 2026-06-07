package it.hackhub.strategy;

import it.hackhub.domain.ClassificaEntry;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class HighestScoreWinnerStrategy implements WinnerSelectionStrategy {
    @Override
    public ClassificaEntry selezionaVincitore(List<ClassificaEntry> classifica) {
        if (classifica == null || classifica.isEmpty()) {
            throw new IllegalStateException("Nessuna sottomissione valutata");
        }
        return classifica.get(0);
    }
}
