package pl.chrapatij.moneytransfer.repository;

import pl.chrapatij.moneytransfer.model.Amount;
import pl.chrapatij.moneytransfer.model.Card;
import pl.chrapatij.moneytransfer.model.Transaction;

import java.util.Optional;

public interface MoneyTransferRepo {
    String transfer(String cardFromNumber, String cardToNumber, int amount, Double commission);

    String confirm(String operationId, String code);
    Optional<Card> getCard(String cardNumber);
    boolean checkBalance(String cardNumber, int amount);
    Optional<Transaction> getTransaction(String operationId);
    boolean checkConfirmCode(String operationId, String confirmCode);
}