package pl.chrapatij.moneytransfer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.chrapatij.moneytransfer.model.*;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MoneyTransferApplicationTests {
    private static final String HOST_NAME = "http://localhost";
    private static final int PORT = 8080;
    private static final String END_POINT = "transfer";
    private static final String END_POINT2 = "confirmOperation";

    @Autowired
    TestRestTemplate myTemplate;

    @Container
    private static final GenericContainer<?> myContainer = new GenericContainer<>("backend:0.0.1")
            .withExposedPorts(PORT)
            .waitingFor(Wait.forHttp("/").forStatusCode(404));

    @Test
    // Успешный перевод денег.
    void transferSuccessful() {
        TransferRequestBody myTransfer = new TransferRequestBody(
                "1111222233334444",
                "09/25",
                "804",
                "2222333344445555",
                new Amount(1000, "RUR")
        );
        HttpEntity<TransferRequestBody> myRequest = new HttpEntity<>(myTransfer);
        ResponseEntity<MoneyTransferResponse> test = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT),
                        myRequest,
                        MoneyTransferResponse.class
                );
        assertEquals(HttpStatus.OK, test.getStatusCode());
    }

    @Test
    // Карта отправителя совпадает с картой получателя.
    void transferError1() {
        TransferRequestBody myTransfer = new TransferRequestBody(
                "1111222233334444",
                "09/25",
                "804",
                "1111222233334444",
                new Amount(1000, "RUR")
        );
        HttpEntity<TransferRequestBody> myRequest = new HttpEntity<>(myTransfer);
        ResponseEntity<MoneyTransferError> test = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT),
                        myRequest,
                        MoneyTransferError.class
                );
        assertEquals("Необходимо указать разные карты для отправителя и получателя.", Objects.requireNonNull(test.getBody()).message());
    }

    @Test
    // Некорректный номер карты отправителя.
    void transferError2() {
        TransferRequestBody myTransfer = new TransferRequestBody(
                "0000000000000000",
                "09/25",
                "804",
                "2222333344445555",
                new Amount(10000, "RUR")
        );
        HttpEntity<TransferRequestBody> myRequest = new HttpEntity<>(myTransfer);
        ResponseEntity<MoneyTransferError> test = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT),
                        myRequest,
                        MoneyTransferError.class
                );
        assertEquals(String.format("Не корректный номер карты: %s", myTransfer.getCardFromNumber()), Objects.requireNonNull(test.getBody()).message());
    }

    @Test
    // Некорректный номер карты получателя.
    void transferError3() {
        TransferRequestBody myTransfer = new TransferRequestBody(
                "1111222233334444",
                "09/25",
                "804",
                "0000000000000000",
                new Amount(10000, "RUR")
        );
        HttpEntity<TransferRequestBody> myRequest = new HttpEntity<>(myTransfer);
        ResponseEntity<MoneyTransferError> test = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT),
                        myRequest,
                        MoneyTransferError.class
                );
        assertEquals(String.format("Не корректный номер карты: %s", myTransfer.getCardToNumber()), Objects.requireNonNull(test.getBody()).message());
    }

    @Test
    // Некорректный период действия карты отправителя.
    void transferError4() {
        TransferRequestBody myTransfer = new TransferRequestBody(
                "1111222233334444",
                "09/24",
                "804",
                "2222333344445555",
                new Amount(10000, "RUR")
        );
        HttpEntity<TransferRequestBody> myRequest = new HttpEntity<>(myTransfer);
        ResponseEntity<MoneyTransferError> test = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT),
                        myRequest,
                        MoneyTransferError.class
                );
        assertEquals(String.format("Не верный период действия карты: %s", myTransfer.getCardFromNumber()), Objects.requireNonNull(test.getBody()).message());
    }

    @Test
    // Некорректный CVV код карты отправителя.
    void transferError5() {
        TransferRequestBody myTransfer = new TransferRequestBody(
                "1111222233334444",
                "09/25",
                "803",
                "2222333344445555",
                new Amount(10000, "RUR")
        );
        HttpEntity<TransferRequestBody> myRequest = new HttpEntity<>(myTransfer);
        ResponseEntity<MoneyTransferError> test = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT),
                        myRequest,
                        MoneyTransferError.class
                );
        assertEquals(String.format("Не верный CVV код карты: %s", myTransfer.getCardFromNumber()), Objects.requireNonNull(test.getBody()).message());
    }

    @Test
    // Не достаточно денег на карте отправителя.
    void transferError6() {
        TransferRequestBody myTransfer = new TransferRequestBody(
                "1111222233334444",
                "09/25",
                "804",
                "2222333344445555",
                new Amount(10000, "RUR")
        );
        HttpEntity<TransferRequestBody> myRequest = new HttpEntity<>(myTransfer);
        ResponseEntity<MoneyTransferError> test = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT),
                        myRequest,
                        MoneyTransferError.class
                );
        assertEquals(String.format("Не достаточно средств на карте: %s", myTransfer.getCardFromNumber()), Objects.requireNonNull(test.getBody()).message());
    }

    @Test
    // Успешный подтверждение перевода денег.
    void confirmSuccessful() {
        TransferRequestBody myTransfer = new TransferRequestBody(
                "1111222233334444",
                "09/25",
                "804",
                "2222333344445555",
                new Amount(1000, "RUR")
        );
        HttpEntity<TransferRequestBody> myRequest = new HttpEntity<>(myTransfer);
        ResponseEntity<MoneyTransferResponse> test = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT),
                        myRequest,
                        MoneyTransferResponse.class
                );
        String operationId = Objects.requireNonNull(test.getBody()).operationId();
        ConfirmRequestBody myConfirm = new ConfirmRequestBody(operationId, "0000");
        HttpEntity<ConfirmRequestBody> myRequest2 = new HttpEntity<>(myConfirm);
        test = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT2),
                        myRequest2,
                        MoneyTransferResponse.class
                );
        assertEquals(HttpStatus.OK, test.getStatusCode());
    }

    @Test
    // Успешная отмена перевода денег.
    void confirmCanceled() {
        TransferRequestBody myTransfer = new TransferRequestBody(
                "1111222233334444",
                "09/25",
                "804",
                "2222333344445555",
                new Amount(1000, "RUR")
        );
        HttpEntity<TransferRequestBody> myRequest = new HttpEntity<>(myTransfer);
        ResponseEntity<MoneyTransferResponse> test = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT),
                        myRequest,
                        MoneyTransferResponse.class
                );
        String operationId = Objects.requireNonNull(test.getBody()).operationId();
        ConfirmRequestBody myConfirm = new ConfirmRequestBody(operationId, null);
        HttpEntity<ConfirmRequestBody> myRequest2 = new HttpEntity<>(myConfirm);
        test = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT2),
                        myRequest2,
                        MoneyTransferResponse.class
                );
        assertEquals(HttpStatus.OK, test.getStatusCode());
    }

    @Test
    // Транакция не найдена.
    void confirmError1() {
        String operationId = UUID.randomUUID().toString();
        ConfirmRequestBody myConfirm = new ConfirmRequestBody(operationId, "0000");
        HttpEntity<ConfirmRequestBody> myRequest2 = new HttpEntity<>(myConfirm);
        ResponseEntity<MoneyTransferError> test = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT2),
                        myRequest2,
                        MoneyTransferError.class
                );
        assertEquals(String.format("Транзакция %s не найдена", operationId), Objects.requireNonNull(test.getBody()).message());
    }

    @Test
    // Не верный код подтверждения.
    void confirmError2() {
        TransferRequestBody myTransfer = new TransferRequestBody(
                "1111222233334444",
                "09/25",
                "804",
                "2222333344445555",
                new Amount(1000, "RUR")
        );
        HttpEntity<TransferRequestBody> myRequest = new HttpEntity<>(myTransfer);
        ResponseEntity<MoneyTransferResponse> test = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT),
                        myRequest,
                        MoneyTransferResponse.class
                );
        String operationId = Objects.requireNonNull(test.getBody()).operationId();
        ConfirmRequestBody myConfirm = new ConfirmRequestBody(operationId, "1111");
        HttpEntity<ConfirmRequestBody> myRequest2 = new HttpEntity<>(myConfirm);
        ResponseEntity<MoneyTransferError> test2 = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT2),
                        myRequest2,
                        MoneyTransferError.class
                );
        assertEquals(String.format("Для транзакции %s не верно указан код подтверждения", operationId), Objects.requireNonNull(test2.getBody()).message());
    }

    @Test
    // Попытка подтверждения ранее подтвержденной транзакции.
    void confirmError3() {
        TransferRequestBody myTransfer = new TransferRequestBody(
                "1111222233334444",
                "09/25",
                "804",
                "2222333344445555",
                new Amount(1000, "RUR")
        );
        HttpEntity<TransferRequestBody> myRequest = new HttpEntity<>(myTransfer);
        ResponseEntity<MoneyTransferResponse> test = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT),
                        myRequest,
                        MoneyTransferResponse.class
                );
        String operationId = Objects.requireNonNull(test.getBody()).operationId();
        ConfirmRequestBody myConfirm = new ConfirmRequestBody(operationId, "0000");
        HttpEntity<ConfirmRequestBody> myRequest2 = new HttpEntity<>(myConfirm);
        test = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT2),
                        myRequest2,
                        MoneyTransferResponse.class
                );
        ResponseEntity<MoneyTransferError> test2 = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT2),
                        myRequest2,
                        MoneyTransferError.class
                );
        assertEquals(String.format("Транзакция %s подтверждена ранее", operationId), Objects.requireNonNull(test2.getBody()).message());
    }

    @Test
    // Попытка подтверждения ранее отмененной транзакции.
    void confirmError4() {
        TransferRequestBody myTransfer = new TransferRequestBody(
                "1111222233334444",
                "09/25",
                "804",
                "2222333344445555",
                new Amount(1000, "RUR")
        );
        HttpEntity<TransferRequestBody> myRequest = new HttpEntity<>(myTransfer);
        ResponseEntity<MoneyTransferResponse> test = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT),
                        myRequest,
                        MoneyTransferResponse.class
                );
        String operationId = Objects.requireNonNull(test.getBody()).operationId();
        ConfirmRequestBody myConfirm = new ConfirmRequestBody(operationId, null);
        HttpEntity<ConfirmRequestBody> myRequest2 = new HttpEntity<>(myConfirm);
        test = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT2),
                        myRequest2,
                        MoneyTransferResponse.class
                );
        myConfirm.setCode("0000");
        ResponseEntity<MoneyTransferError> test2 = myTemplate
                .postForEntity(
                        String.format("%s:%s/%s", HOST_NAME, myContainer.getMappedPort(PORT), END_POINT2),
                        myRequest2,
                        MoneyTransferError.class
                );
        assertEquals(String.format("Транзакция %s отменена ранее", operationId), Objects.requireNonNull(test2.getBody()).message());
    }
}