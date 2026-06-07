package it.hackhub.cli;

import java.time.LocalDateTime;
import java.util.Scanner;

public class LettoreInput {
    private final Scanner scanner;

    public LettoreInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public String stringa(String messaggio) {
        System.out.print(messaggio);
        return scanner.nextLine();
    }

    public Long longValue(String messaggio) {
        return Long.parseLong(stringa(messaggio));
    }

    public int intValue(String messaggio) {
        return Integer.parseInt(stringa(messaggio));
    }

    public LocalDateTime dataOra(String messaggio) {
        return LocalDateTime.parse(stringa(messaggio + " [yyyy-MM-ddTHH:mm]: "));
    }
}
