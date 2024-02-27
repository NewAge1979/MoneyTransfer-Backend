package pl.chrapatij.moneytransfer.repository;

import org.springframework.stereotype.Repository;
import pl.chrapatij.moneytransfer.model.Card;
import pl.chrapatij.moneytransfer.model.Transaction;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MoneyTransferRepoImpl implements MoneyTransferRepo {
    private final Map<String, Card> cards = new ConcurrentHashMap<>();
    private final Map<String, Transaction> transactions = new ConcurrentHashMap<>();

    @Override
    public void addCard(Card card) {
        cards.put(card.getNumber(), card);
    }

    @Override
    public Optional<Card> getCard(String cardNumber) {
        return Optional.ofNullable(cards.get(cardNumber));
    }

    @Override
    public void addTransaction(Transaction transaction) {
        transactions.put(transaction.getOperationId(), transaction);
    }

    @Override
    public Optional<Transaction> getTransaction(String operationId) {
        return Optional.ofNullable(transactions.get(operationId));
    }
}