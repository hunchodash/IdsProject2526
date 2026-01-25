package it.hackhub.adapter;
import org.springframework.stereotype.Component;

@Component

public class StripePaymentAdapter implements PaymentAdapter {

    @Override
    public boolean erogaPremio(Long teamId, double importo) {
        System.out.println("Stripe Gateway: Erogazione premio di " + importo + " al team " + teamId);
        return true;
    }
}