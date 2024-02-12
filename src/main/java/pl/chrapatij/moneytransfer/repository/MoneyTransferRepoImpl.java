package pl.chrapatij.moneytransfer.repository;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Repository;
import pl.chrapatij.moneytransfer.model.Amount;
import pl.chrapatij.moneytransfer.model.Card;
import pl.chrapatij.moneytransfer.model.Transaction;
import pl.chrapatij.moneytransfer.model.TransactionStatus;
import pl.chrapatij.moneytransfer.service.MoneyTransferSrvImpl;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MoneyTransferRepoImpl implements MoneyTransferRepo {
    private final Map<String, Card> cards = new ConcurrentHashMap<>();
    private final Map<String, Transaction> transactions = new ConcurrentHashMap<>();
    private static final Logger myLogger = LoggerFactory.getLogger(MoneyTransferSrvImpl.class);
    private static final Marker myMarker = MarkerFactory.getMarker("MoneyTransfer");

    public MoneyTransferRepoImpl() {
        cards.put(
                "1111222233334444",
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
        cards.put(
                "2222333344445555",
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
        cards.put(
                "3333444455556666",
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
    }

    @Override
    public String transfer(String cardFromNumber, String cardToNumber, int amount, Double commission) {
        if (getCard(cardFromNumber).isPresent() && getCard(cardToNumber).isPresent()) {
            int delta = (int) (amount + amount * commission);
            cards.get(cardFromNumber).setBalance(cards.get(cardFromNumber).getBalance() - delta);
            cards.get(cardToNumber).setBalance(cards.get(cardToNumber).getBalance() + delta);
            String operationId = UUID.randomUUID().toString();
            String confirmCode = "0000";//RandomStringUtils.randomNumeric(6);
            myLogger.info(myMarker, "Operation Id: {}, confirm code: {}", operationId, confirmCode);
            transactions.put(operationId, new Transaction(operationId, confirmCode, TransactionStatus.NEW));
            return operationId;
        }
        return null;
    }

    @Override
    public String confirm(String operationId, String code) {
        Optional<Transaction> transaction = getTransaction(operationId);
        if (transaction.isPresent()) {
            if (checkConfirmCode(operationId, code)) {
                transaction.get().setStatus(TransactionStatus.SUCCESSFULED);
            } else {
                transaction.get().setStatus(TransactionStatus.ERROR);
            }
        }
        return operationId;
    }

    @Override
    public Optional<Card> getCard(String cardNumber) {
        return Optional.ofNullable(cards.get(cardNumber));
    }

    @Override
    public boolean checkBalance(String cardNumber, int amount) {
        return getCard(cardNumber).filter(x -> x.getBalance() + x.getLimit() - amount >= 0).isPresent();
    }

    @Override
    public Optional<Transaction> getTransaction(String operationId) {
        return Optional.ofNullable(transactions.get(operationId));
    }

    @Override
    public boolean checkConfirmCode(String operationId, String confirmCode) {
        return getTransaction(operationId).filter(x -> x.getConfirmCode().equals(confirmCode)).isPresent();
    }
}