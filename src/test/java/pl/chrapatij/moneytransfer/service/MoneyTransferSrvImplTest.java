package pl.chrapatij.moneytransfer.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import pl.chrapatij.moneytransfer.model.MoneyTransferResponse;
import pl.chrapatij.moneytransfer.model.Transaction;
import pl.chrapatij.moneytransfer.model.TransactionStatus;
import pl.chrapatij.moneytransfer.repository.MoneyTransferRepo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static pl.chrapatij.moneytransfer.DataForTest.*;

@SpringBootTest
class MoneyTransferSrvImplTest {
    //private final MoneyTransferRepo moneyTransferRepoMock = Mockito.mock(MoneyTransferRepo.class);
    @Mock
    private MoneyTransferRepo moneyTransferRepoMock;

    @Test
    public void testTransfer() {
        Mockito.when(moneyTransferRepoMock.getCard(CARD1_NUM)).thenReturn(Optional.of(CARD1));
        Mockito.when(moneyTransferRepoMock.getCard(CARD2_NUM)).thenReturn(Optional.of(CARD2));
        int amount = (int) (TRB1.getAmount().getValue() * 1.01);
        Mockito.when(moneyTransferRepoMock.checkBalance(CARD1_NUM, amount)).thenReturn(true);
        Mockito.when(moneyTransferRepoMock.transfer(CARD1_NUM, CARD2_NUM, 5000, 0.01)).thenReturn(OPERATION_ID);
        MoneyTransferSrv moneyTransferSrv = new MoneyTransferSrvImpl(moneyTransferRepoMock);
        assertEquals(new MoneyTransferResponse(OPERATION_ID), moneyTransferSrv.transfer(TRB1));
    }

    @Test
    public void testConfirm() {
        Mockito.when(moneyTransferRepoMock.getTransaction(OPERATION_ID)).thenReturn(Optional.of(new Transaction(OPERATION_ID, CONFIRM_CODE, TransactionStatus.NEW)));
        Mockito.when(moneyTransferRepoMock.checkConfirmCode(OPERATION_ID, CONFIRM_CODE)).thenReturn(true);
        Mockito.when(moneyTransferRepoMock.confirm(OPERATION_ID, CONFIRM_CODE)).thenReturn(OPERATION_ID);
        MoneyTransferSrv moneyTransferSrv = new MoneyTransferSrvImpl(moneyTransferRepoMock);
        assertEquals(new MoneyTransferResponse(OPERATION_ID), moneyTransferSrv.confirm(CRB1));
    }
}