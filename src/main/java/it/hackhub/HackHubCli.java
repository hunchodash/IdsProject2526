package it.hackhub;

import it.hackhub.cli.DatiDimostrativiRunner;
import it.hackhub.cli.MainMenu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class HackHubCli implements CommandLineRunner {
    private final DatiDimostrativiRunner datiDimostrativiRunner;
    private final MainMenu mainMenu;

    public HackHubCli(DatiDimostrativiRunner datiDimostrativiRunner, MainMenu mainMenu) {
        this.datiDimostrativiRunner = datiDimostrativiRunner;
        this.mainMenu = mainMenu;
    }

    @Override
    public void run(String... args) {
        datiDimostrativiRunner.carica();
        mainMenu.avvia();
    }
}
