package it.hackhub.adapter;

import it.hackhub.domain.Team;
import org.springframework.stereotype.Component;

@Component
public class FakePaymentAdapter implements PaymentAdapter {

    @Override
    public boolean erogaPremio(Team team, double importo) {
        if (team == null || importo < 0) {
            System.out.println("[Pagamento fake] Erogazione premio fallita.");
            return false;
        }

        System.out.println("[Pagamento fake] Premio di " + importo + " euro erogato al team: " + team.getNomeTeam());
        return true;
    }
}