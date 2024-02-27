package pl.chrapatij.moneytransfer.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import pl.chrapatij.moneytransfer.exception.ConfirmException;
import pl.chrapatij.moneytransfer.exception.InputDataException;
import pl.chrapatij.moneytransfer.exception.TransferException;
import pl.chrapatij.moneytransfer.model.ConfirmRequestBody;
import pl.chrapatij.moneytransfer.model.MoneyTransferResponse;
import pl.chrapatij.moneytransfer.model.Transaction;
import pl.chrapatij.moneytransfer.model.TransactionStatus;
import pl.chrapatij.moneytransfer.repository.MoneyTransferRepo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static pl.chrapatij.moneytransfer.DataForTest.*;

@SpringBootTest
class MoneyTransferSrvImplTest {
    @Mock
    private ValidateData validateDataMock;
    @Mock
    private MoneyTransferRepo moneyTransferRepoMock;

    @Test
    // Успешный перевод денег.
    public void testTransferSuccessful() {
        Mockito.when(moneyTransferRepoMock.getCard(CARD1_NUM)).thenReturn(Optional.of(CARD1));
        Mockito.when(moneyTransferRepoMock.getCard(CARD2_NUM)).thenReturn(Optional.of(CARD2));
        int amount = TRB1.getAmount().getValue();
        Mockito.when(validateDataMock.checkBalance(CARD1, amount, COMMISSION_PERCENT)).thenReturn(true);
        MoneyTransferSrv moneyTransferSrv = new MoneyTransferSrvImpl(moneyTransferRepoMock);
        assertNotNull(moneyTransferSrv.transfer(TRB1));
    }

    @Test
    // Карта отправителя и получателя совпадают.
    public void testTransferError1() {
        Mockito.when(moneyTransferRepoMock.getCard(CARD1_NUM)).thenReturn(Optional.of(CARD1));
        int amount = TRB2.getAmount().getValue();
        Mockito.when(validateDataMock.checkBalance(CARD1, amount, COMMISSION_PERCENT)).thenReturn(true);
        MoneyTransferSrv moneyTransferSrv = new MoneyTransferSrvImpl(moneyTransferRepoMock);
        assertThrows(InputDataException.class, () -> {
            MoneyTransferResponse transfer = moneyTransferSrv.transfer(TRB2);
        });
    }

    @Test
    // Ошибочная карта отправителя.
    public void testTransferError2() {
        Mockito.when(moneyTransferRepoMock.getCard(CARD1_NUM)).thenReturn(Optional.of(CARD1));
        Mockito.when(moneyTransferRepoMock.getCard(CARD2_NUM)).thenReturn(Optional.of(CARD2));
        int amount = TRB3.getAmount().getValue();
        Mockito.when(validateDataMock.checkBalance(CARD1, amount, COMMISSION_PERCENT)).thenReturn(true);
        MoneyTransferSrv moneyTransferSrv = new MoneyTransferSrvImpl(moneyTransferRepoMock);
        assertThrows(InputDataException.class, () -> {
            MoneyTransferResponse transfer = moneyTransferSrv.transfer(TRB3);
        });
    }

    @Test
    // Ошибочная карта получателя.
    public void testTransferError3() {
        Mockito.when(moneyTransferRepoMock.getCard(CARD1_NUM)).thenReturn(Optional.of(CARD1));
        Mockito.when(moneyTransferRepoMock.getCard(CARD2_NUM)).thenReturn(Optional.of(CARD2));
        int amount = TRB4.getAmount().getValue();
        Mockito.when(validateDataMock.checkBalance(CARD1, amount, COMMISSION_PERCENT)).thenReturn(true);
        MoneyTransferSrv moneyTransferSrv = new MoneyTransferSrvImpl(moneyTransferRepoMock);
        assertThrows(InputDataException.class, () -> {
            MoneyTransferResponse transfer = moneyTransferSrv.transfer(TRB4);
        });
    }

    @Test
    // Ошибка в периоде действия карты отправителя.
    public void testTransferError4() {
        Mockito.when(moneyTransferRepoMock.getCard(CARD1_NUM)).thenReturn(Optional.of(CARD1));
        Mockito.when(moneyTransferRepoMock.getCard(CARD2_NUM)).thenReturn(Optional.of(CARD2));
        int amount = TRB5.getAmount().getValue();
        Mockito.when(validateDataMock.checkBalance(CARD1, amount, COMMISSION_PERCENT)).thenReturn(true);
        MoneyTransferSrv moneyTransferSrv = new MoneyTransferSrvImpl(moneyTransferRepoMock);
        assertThrows(InputDataException.class, () -> {
            MoneyTransferResponse transfer = moneyTransferSrv.transfer(TRB5);
        });
    }

    @Test
    // Ошибка в CVV коде карты отправителя.
    public void testTransferError5() {
        Mockito.when(moneyTransferRepoMock.getCard(CARD1_NUM)).thenReturn(Optional.of(CARD1));
        Mockito.when(moneyTransferRepoMock.getCard(CARD2_NUM)).thenReturn(Optional.of(CARD2));
        int amount = TRB6.getAmount().getValue();
        Mockito.when(validateDataMock.checkBalance(CARD1, amount, COMMISSION_PERCENT)).thenReturn(true);
        MoneyTransferSrv moneyTransferSrv = new MoneyTransferSrvImpl(moneyTransferRepoMock);
        assertThrows(InputDataException.class, () -> {
            MoneyTransferResponse transfer = moneyTransferSrv.transfer(TRB6);
        });
    }

    @Test
    // Недостаточно средств на карте отправтеля.
    public void testTransferError6() {
        Mockito.when(moneyTransferRepoMock.getCard(CARD1_NUM)).thenReturn(Optional.of(CARD1));
        Mockito.when(moneyTransferRepoMock.getCard(CARD2_NUM)).thenReturn(Optional.of(CARD2));
        int amount = TRB7.getAmount().getValue();
        Mockito.when(validateDataMock.checkBalance(CARD1, amount, COMMISSION_PERCENT)).thenReturn(true);
        MoneyTransferSrv moneyTransferSrv = new MoneyTransferSrvImpl(moneyTransferRepoMock);
        assertThrows(TransferException.class, () -> {
            MoneyTransferResponse transfer = moneyTransferSrv.transfer(TRB7);
        });
    }

    @Test
    // Успешное подтверждение транзакции.
    public void testConfirmSuccessful() {
        Mockito.when(moneyTransferRepoMock.getCard(CARD1_NUM)).thenReturn(Optional.of(CARD1));
        Mockito.when(moneyTransferRepoMock.getCard(CARD2_NUM)).thenReturn(Optional.of(CARD2));
        int amount = TRB1.getAmount().getValue();
        Mockito.when(validateDataMock.checkBalance(CARD1, amount, COMMISSION_PERCENT)).thenReturn(true);
        MoneyTransferSrv moneyTransferSrv = new MoneyTransferSrvImpl(moneyTransferRepoMock);
        String operationId = moneyTransferSrv.transfer(TRB1).operationId();
        Mockito.when(moneyTransferRepoMock.getTransaction(operationId))
                .thenReturn(Optional.of(new Transaction(operationId, "0000", TransactionStatus.NEW)));
        Transaction transaction = moneyTransferRepoMock.getTransaction(operationId).get();
        Mockito.when(validateDataMock.checkConfirmCode(transaction, "0000")).thenReturn(true);
        ConfirmRequestBody CRB1 = new ConfirmRequestBody(operationId, "0000");
        assertEquals(new MoneyTransferResponse(operationId), moneyTransferSrv.confirm(CRB1));
    }

    @Test
    // Успешная отмена транзакции.
    public void testConfirmCanceled() {
        Mockito.when(moneyTransferRepoMock.getCard(CARD1_NUM)).thenReturn(Optional.of(CARD1));
        Mockito.when(moneyTransferRepoMock.getCard(CARD2_NUM)).thenReturn(Optional.of(CARD2));
        int amount = TRB1.getAmount().getValue();
        Mockito.when(validateDataMock.checkBalance(CARD1, amount, COMMISSION_PERCENT)).thenReturn(true);
        MoneyTransferSrv moneyTransferSrv = new MoneyTransferSrvImpl(moneyTransferRepoMock);
        String operationId = moneyTransferSrv.transfer(TRB1).operationId();
        Mockito.when(moneyTransferRepoMock.getTransaction(operationId))
                .thenReturn(Optional.of(new Transaction(operationId, "0000", TransactionStatus.NEW)));
        Transaction transaction = moneyTransferRepoMock.getTransaction(operationId).get();
        Mockito.when(validateDataMock.checkConfirmCode(transaction, "0000")).thenReturn(true);
        ConfirmRequestBody CRB1 = new ConfirmRequestBody(operationId, null);
        assertEquals(new MoneyTransferResponse(operationId), moneyTransferSrv.confirm(CRB1));
    }

    @Test
    // Транзакция не найдена.
    public void testConfirmError1() {
        Mockito.when(moneyTransferRepoMock.getCard(CARD1_NUM)).thenReturn(Optional.of(CARD1));
        Mockito.when(moneyTransferRepoMock.getCard(CARD2_NUM)).thenReturn(Optional.of(CARD2));
        int amount = TRB1.getAmount().getValue();
        Mockito.when(validateDataMock.checkBalance(CARD1, amount, COMMISSION_PERCENT)).thenReturn(true);
        MoneyTransferSrv moneyTransferSrv = new MoneyTransferSrvImpl(moneyTransferRepoMock);
        String operationId = moneyTransferSrv.transfer(TRB1).operationId();
        Mockito.when(moneyTransferRepoMock.getTransaction(operationId))
                .thenReturn(Optional.of(new Transaction(operationId, "0000", TransactionStatus.NEW)));
        Transaction transaction = moneyTransferRepoMock.getTransaction(operationId).get();
        Mockito.when(validateDataMock.checkConfirmCode(transaction, "0000")).thenReturn(true);
        ConfirmRequestBody CRB1 = new ConfirmRequestBody(operationId + "0000", "0000");
        assertThrows(ConfirmException.class, () -> {
            MoneyTransferResponse confirm = moneyTransferSrv.confirm(CRB1);
        });
    }

    @Test
    // Попытка подтвердить ранее подтвержденную транзакцию.
    public void testConfirmError2() {
        Mockito.when(moneyTransferRepoMock.getCard(CARD1_NUM)).thenReturn(Optional.of(CARD1));
        Mockito.when(moneyTransferRepoMock.getCard(CARD2_NUM)).thenReturn(Optional.of(CARD2));
        int amount = TRB1.getAmount().getValue();
        Mockito.when(validateDataMock.checkBalance(CARD1, amount, COMMISSION_PERCENT)).thenReturn(true);
        MoneyTransferSrv moneyTransferSrv = new MoneyTransferSrvImpl(moneyTransferRepoMock);
        String operationId = moneyTransferSrv.transfer(TRB1).operationId();
        Mockito.when(moneyTransferRepoMock.getTransaction(operationId))
                .thenReturn(Optional.of(new Transaction(operationId, "0000", TransactionStatus.SUCCESSFULED)));
        Transaction transaction = moneyTransferRepoMock.getTransaction(operationId).get();
        Mockito.when(validateDataMock.checkConfirmCode(transaction, "0000")).thenReturn(true);
        ConfirmRequestBody CRB1 = new ConfirmRequestBody(operationId, "0000");
        assertThrows(ConfirmException.class, () -> {
            MoneyTransferResponse confirm = moneyTransferSrv.confirm(CRB1);
        });
    }

    @Test
    // Попытка подтвердить ранее отмененную транзакцию.
    public void testConfirmError3() {
        Mockito.when(moneyTransferRepoMock.getCard(CARD1_NUM)).thenReturn(Optional.of(CARD1));
        Mockito.when(moneyTransferRepoMock.getCard(CARD2_NUM)).thenReturn(Optional.of(CARD2));
        int amount = TRB1.getAmount().getValue();
        Mockito.when(validateDataMock.checkBalance(CARD1, amount, COMMISSION_PERCENT)).thenReturn(true);
        MoneyTransferSrv moneyTransferSrv = new MoneyTransferSrvImpl(moneyTransferRepoMock);
        String operationId = moneyTransferSrv.transfer(TRB1).operationId();
        Mockito.when(moneyTransferRepoMock.getTransaction(operationId))
                .thenReturn(Optional.of(new Transaction(operationId, "0000", TransactionStatus.CANCELED)));
        Transaction transaction = moneyTransferRepoMock.getTransaction(operationId).get();
        Mockito.when(validateDataMock.checkConfirmCode(transaction, "0000")).thenReturn(true);
        ConfirmRequestBody CRB1 = new ConfirmRequestBody(operationId, "0000");
        assertThrows(ConfirmException.class, () -> {
            MoneyTransferResponse confirm = moneyTransferSrv.confirm(CRB1);
        });
    }

    @Test
    // Не правильный код подтверждения транзакции.
    public void testConfirmError4() {
        Mockito.when(moneyTransferRepoMock.getCard(CARD1_NUM)).thenReturn(Optional.of(CARD1));
        Mockito.when(moneyTransferRepoMock.getCard(CARD2_NUM)).thenReturn(Optional.of(CARD2));
        int amount = TRB1.getAmount().getValue();
        Mockito.when(validateDataMock.checkBalance(CARD1, amount, COMMISSION_PERCENT)).thenReturn(true);
        MoneyTransferSrv moneyTransferSrv = new MoneyTransferSrvImpl(moneyTransferRepoMock);
        String operationId = moneyTransferSrv.transfer(TRB1).operationId();
        Mockito.when(moneyTransferRepoMock.getTransaction(operationId))
                .thenReturn(Optional.of(new Transaction(operationId, "0000", TransactionStatus.NEW)));
        Transaction transaction = moneyTransferRepoMock.getTransaction(operationId).get();
        Mockito.when(validateDataMock.checkConfirmCode(transaction, "0000")).thenReturn(true);
        ConfirmRequestBody CRB1 = new ConfirmRequestBody(operationId, "1111");
        assertThrows(ConfirmException.class, () -> {
            MoneyTransferResponse confirm = moneyTransferSrv.confirm(CRB1);
        });
    }
}