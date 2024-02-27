package pl.chrapatij.moneytransfer.repository;

import pl.chrapatij.moneytransfer.model.Card;
import pl.chrapatij.moneytransfer.model.Transaction;

import java.util.Optional;

public interface MoneyTransferRepo {
    void addCard(Card card);

    Optional<Card> getCard(String cardNumber);

    void addTransaction(Transaction transaction);

    Optional<Transaction> getTransaction(String operationId);
}