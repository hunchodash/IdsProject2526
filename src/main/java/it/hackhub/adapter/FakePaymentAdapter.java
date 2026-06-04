package it.hackhub.adapter;

import it.hackhub.domain.Team;
import org.springframework.stereotype.Component;

@Component
public class FakePaymentAdapter implements PaymentAdapter {
    public boolean erogaPremio(Team team, double importo) {
        return team != null && importo >= 0;
    }
}
