package pl.chrapatij.moneytransfer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.chrapatij.moneytransfer.model.Amount;
import pl.chrapatij.moneytransfer.model.MoneyTransferError;
import pl.chrapatij.moneytransfer.model.MoneyTransferResponse;
import pl.chrapatij.moneytransfer.model.TransferRequestBody;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MoneyTransferApplicationTests {
    private static final String HOST_NAME = "http://localhost";
    private static final int PORT = 8080;
    private static final String END_POINT = "transfer";

    @Autowired
    TestRestTemplate myTemplate;

    @Container
    private static final GenericContainer<?> myContainer = new GenericContainer<>("backend:0.0.1")
            .withExposedPorts(PORT)
            .waitingFor(Wait.forHttp("/").forStatusCode(404));

    @Test
    void contextLoads() {
        System.out.println(String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT));
        System.out.println(myContainer.getContainerName());
        Amount amount = new Amount(1000, "RUR");
        TransferRequestBody myTransfer = new TransferRequestBody(
                "1111222233334444",
                "09/25",
                "804",
                "2222333344445555",
                amount
        );
        ResponseEntity<MoneyTransferResponse> test = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT),
                        myTransfer,
                        MoneyTransferResponse.class
                );
        assertEquals(HttpStatus.OK, test.getStatusCode());
        /*ResponseEntity<MoneyTransferError> error = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT),
                        myTransfer,
                        MoneyTransferError.class
                );
        System.out.println(error);*/
    }

}