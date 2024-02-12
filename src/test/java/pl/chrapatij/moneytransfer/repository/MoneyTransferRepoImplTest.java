package pl.chrapatij.moneytransfer.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import pl.chrapatij.moneytransfer.model.Card;

import static org.junit.jupiter.api.Assertions.*;
import static pl.chrapatij.moneytransfer.DataForTest.*;

class MoneyTransferRepoImplTest {
    private MoneyTransferRepo testRepository;
    private final Map<String, Card> testCards = new ConcurrentHashMap<>();

    @BeforeEach
    void setUp() {
        testRepository = new MoneyTransferRepoImpl();
        testCards.put(CARD1_NUM, CARD1);
        testCards.put(CARD2_NUM, CARD2);
    }

    @ParameterizedTest
    @ValueSource(strings = {CARD1_NUM, CARD2_NUM})
    public void testGetCard(String cardNum) {
        assertEquals(testCards.get(cardNum), testRepository.getCard(cardNum).get());
    }

    @Test
    public void testCheckBalance() {
        assertTrue(testRepository.checkBalance(CARD1_NUM, 5000));
    }

    @Test
    public void testTransaction() {
        String operationId = testRepository.transfer(CARD1_NUM, CARD2_NUM, 5000, 0.01);
        assertTrue(testRepository.getTransaction(operationId).isPresent());
    }

    @Test
    public void testCheckConfirmCode() {
        String operationId = testRepository.transfer(CARD1_NUM, CARD2_NUM, 5000, 0.01);
        assertTrue(testRepository.checkConfirmCode(operationId, "0000"));
    }
}