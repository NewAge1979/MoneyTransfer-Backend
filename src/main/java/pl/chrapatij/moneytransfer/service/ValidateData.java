package pl.chrapatij.moneytransfer.service;

import pl.chrapatij.moneytransfer.model.Card;
import pl.chrapatij.moneytransfer.model.Transaction;

public class ValidateData {
    public boolean checkCardNumber(String card1Number, String card2Number) {
        return card1Number.equals(card2Number);
    }

    public boolean checkValidTill(Card card, String validTill) {
        return card.getValidTill().equals(validTill);
    }

    public boolean checkCVV(Card card, String curCVV) {
        return card.getCvv().equals(curCVV);
    }

    public boolean checkBalance(Card card, int amount, Double commission) {
        return (card.getBalance() + (card.isCreditCard() ? card.getLimit() : 0) >= (amount * (1 + commission)));
    }

    public boolean checkConfirmCode(Transaction transaction, String confirmCode) {
        return transaction.getConfirmCode().equals(confirmCode);
    }
}