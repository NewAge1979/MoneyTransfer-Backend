package pl.chrapatij.moneytransfer.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;
import pl.chrapatij.moneytransfer.exception.ConfirmException;
import pl.chrapatij.moneytransfer.exception.InputDataException;
import pl.chrapatij.moneytransfer.exception.TransferException;
import pl.chrapatij.moneytransfer.model.*;
import pl.chrapatij.moneytransfer.repository.MoneyTransferRepo;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MoneyTransferSrvImpl implements MoneyTransferSrv {
    private final MoneyTransferRepo moneyTransferRepo;
    private static final Logger myLogger = LoggerFactory.getLogger(MoneyTransferSrvImpl.class);
    private static final Marker myMarker = MarkerFactory.getMarker("MoneyTransfer");

    @Override
    public MoneyTransferResponse transfer(TransferRequestBody transferRequestBody) {
        Optional<Card> cardFrom = moneyTransferRepo.getCard(transferRequestBody.getCardFromNumber());
        Optional<Card> cardTo = moneyTransferRepo.getCard(transferRequestBody.getCardToNumber());

        String cardFromNumber = transferRequestBody.getCardFromNumber();
        String cardToNumber = transferRequestBody.getCardToNumber();
        int amountValue = transferRequestBody.getAmount().getValue();

        Double PERCENT = 0.01;
        if (cardFrom.isPresent() && cardTo.isPresent() && !cardFrom.get().equals(cardTo.get())) {
            Card cardF = cardFrom.get();
            if (!cardF.getValidTill().equals(transferRequestBody.getCardFromValidTill())) {
                String msg = String.format("Invalid valid date for card: %s", cardFromNumber);
                addStringInLog(cardFromNumber, cardToNumber, amountValue, PERCENT, "", String.valueOf(TransferStatus.INPUTDATA_ERROR), msg);
                throw new InputDataException(msg);
            }
            if (!cardF.getCvv().equals(transferRequestBody.getCardFromCVV())) {
                String msg = String.format("Invalid CVV for card: %s", cardFromNumber);
                addStringInLog(cardFromNumber, cardToNumber, amountValue, PERCENT, "", String.valueOf(TransferStatus.INPUTDATA_ERROR), msg);
                throw new InputDataException(msg);
            }
            if (!moneyTransferRepo.checkBalance(cardFromNumber, (int) (amountValue + amountValue * PERCENT))) {
                String msg = String.format("Not enough money on card: %s", cardFromNumber);
                addStringInLog(cardFromNumber, cardToNumber, amountValue, PERCENT, "", String.valueOf(TransferStatus.TRANSFER_ERROR), msg);
                throw new TransferException(msg);
            }
            String operationId = moneyTransferRepo.transfer(cardFromNumber, cardToNumber, amountValue, PERCENT);
            addStringInLog(cardFromNumber, cardToNumber, amountValue, PERCENT, operationId, String.valueOf(TransferStatus.SUCCESSFULED), "");
            return new MoneyTransferResponse(operationId);
        } else {
            if (cardFrom.isEmpty()) {
                String msg = String.format("Invalid card: %s", cardFromNumber);
                addStringInLog(cardFromNumber, cardToNumber, amountValue, PERCENT, "", String.valueOf(TransferStatus.INPUTDATA_ERROR), msg);
                throw new InputDataException(msg);
            }
            if (cardTo.isEmpty()) {
                String msg = String.format("Invalid card: %s", cardToNumber);
                addStringInLog(cardFromNumber, cardToNumber, amountValue, PERCENT, "", String.valueOf(TransferStatus.INPUTDATA_ERROR), msg);
                throw new InputDataException(msg);
            }
            if (cardFrom.get().equals(cardTo.get())) {
                String msg = "It's the same card.";
                addStringInLog(cardFromNumber, cardToNumber, amountValue, PERCENT, "", String.valueOf(TransferStatus.INPUTDATA_ERROR), msg);
                throw new InputDataException(msg);
            }
            return null;
        }
    }

    @Override
    public MoneyTransferResponse confirm(ConfirmRequestBody confirmRequestBody) {
        System.out.println(confirmRequestBody);
        if (moneyTransferRepo.getTransaction(confirmRequestBody.getOperationId()).isPresent()) {
            if (moneyTransferRepo.checkConfirmCode(confirmRequestBody.getOperationId(), confirmRequestBody.getCode())) {
                String msg = String.format("Transaction: %s confirmed", confirmRequestBody.getOperationId());
                addStringInLog(confirmRequestBody.getOperationId(), String.valueOf(TransferStatus.SUCCESSFULED), msg);
                return new MoneyTransferResponse(moneyTransferRepo.confirm(confirmRequestBody.getOperationId(), confirmRequestBody.getCode()));
            } else {
                String msg = String.format("Invalid confirm code (%s) for transaction: %s", confirmRequestBody.getCode(), confirmRequestBody.getOperationId());
                addStringInLog(confirmRequestBody.getOperationId(), String.valueOf(TransferStatus.CONFIRM_ERROR), msg);
                throw new ConfirmException(msg);
            }
        } else {
            String msg = String.format("Invalid transaction: %s", confirmRequestBody.getOperationId());
            addStringInLog(confirmRequestBody.getOperationId(), String.valueOf(TransferStatus.CONFIRM_ERROR), msg);
            throw new ConfirmException(msg);
        }
    }

    private void addStringInLog(
            String cardFrom,
            String cardTo,
            int amount,
            Double commission,
            String operationId,
            String status,
            String comment
    ) {
        String msgForLog = String.format(
                "Debit card: %s, credit card: %s, amount: %d, commission: %d, transaction ID: %s, status: %s, comment: %s",
                cardFrom, cardTo, amount, (int) (amount * commission), operationId, status, comment
        );
        myLogger.info(myMarker, msgForLog);
    }

    private void addStringInLog(String operationId, String status, String comment) {
        String msgForLog = String.format("Transaction ID: %s, status: %s, comment: %s", operationId, status, comment);
        myLogger.info(myMarker, msgForLog);
    }
}