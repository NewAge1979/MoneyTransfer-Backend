package pl.chrapatij.moneytransfer;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.chrapatij.moneytransfer.model.Card;
import pl.chrapatij.moneytransfer.repository.MoneyTransferRepo;

@SpringBootApplication
public class MoneyTransferApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneyTransferApplication.class, args);
    }

    @Bean
    public ApplicationRunner runner(MoneyTransferRepo moneyTransferRepo) {
        return (args) -> {
            moneyTransferRepo.addCard(
                    new Card(
                            "1111222233334444",
                            "09/25",
                            "804",
                            false,
                            10000,
                            0,
                            "RUR"
                    )
            );
            moneyTransferRepo.addCard(
                    new Card(
                            "2222333344445555",
                            "04/27",
                            "567",
                            true,
                            15000,
                            5000,
                            "RUR"
                    )
            );
            moneyTransferRepo.addCard(
                    new Card(
                            "3333444455556666",
                            "08/26",
                            "245",
                            false,
                            10000,
                            0,
                            "RUR"
                    )
            );
            System.out.println("Test!!!");
        };
    }
}