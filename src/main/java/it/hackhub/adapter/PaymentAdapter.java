package it.hackhub.adapter;

import it.hackhub.domain.Team;

public interface PaymentAdapter {
    boolean erogaPremio(Team team, double importo);
}
