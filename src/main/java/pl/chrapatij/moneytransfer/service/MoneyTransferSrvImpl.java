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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MoneyTransferSrvImpl implements MoneyTransferSrv {
    private final MoneyTransferRepo moneyTransferRepo;
    private static final Logger myLogger = LoggerFactory.getLogger(MoneyTransferSrvImpl.class);
    private static final Marker myMarker = MarkerFactory.getMarker("MoneyTransfer");
    private static final ValidateData validateData = new ValidateData();

    @Override
    public MoneyTransferResponse transfer(TransferRequestBody transferRequestBody) throws InputDataException, TransferException {
        String cardFNum = transferRequestBody.getCardFromNumber();
        String cardTNum = transferRequestBody.getCardToNumber();
        int amount = transferRequestBody.getAmount().getValue();
        Optional<Card> cardFrom = moneyTransferRepo.getCard(cardFNum);
        Optional<Card> cardTo = moneyTransferRepo.getCard(cardTNum);

        String operationId = "";
        TransferStatus transferStatus = TransferStatus.INPUTDATA_ERROR;
        String errorMessage = "";
        Double COMMISSION_PERCENT = 0.01;

        if (cardFrom.isPresent() && cardTo.isPresent()) {
            Card card = cardFrom.get();
            if (validateData.checkCardNumber(cardFNum, cardTNum)) {
                errorMessage = "Необходимо указать разные карты для отправителя и получателя.";
            } else {
                if (validateData.checkValidTill(card, transferRequestBody.getCardFromValidTill())) {
                    if (validateData.checkCVV(card, transferRequestBody.getCardFromCVV())) {
                        if (validateData.checkBalance(card, amount, COMMISSION_PERCENT)) {
                            operationId = UUID.randomUUID().toString();
                            transferStatus = TransferStatus.SUCCESSFULED;
                            moneyTransferRepo.addTransaction(new Transaction(operationId, "0000", TransactionStatus.NEW));
                        } else {
                            errorMessage = String.format("Не достаточно средств на карте: %s", cardFNum);
                            transferStatus = TransferStatus.TRANSFER_ERROR;
                        }
                    } else {
                        errorMessage = String.format("Не верный CVV код карты: %s", cardFNum);
                    }
                } else {
                    errorMessage = String.format("Не верный период действия карты: %s", cardFNum);
                }
            }
        } else {
            errorMessage = String.format("Не корректный номер карты: %s", cardFrom.isEmpty() ? cardFNum : cardTNum);
        }
        addStringInLog(cardFNum, cardTNum, amount, COMMISSION_PERCENT, operationId, String.valueOf(transferStatus), errorMessage);
        switch (transferStatus) {
            case INPUTDATA_ERROR -> throw new InputDataException(errorMessage);
            case TRANSFER_ERROR -> throw new TransferException(errorMessage);
        }
        return new MoneyTransferResponse(operationId);
    }

    @Override
    public MoneyTransferResponse confirm(ConfirmRequestBody confirmRequestBody) throws ConfirmException {
        String operationId = confirmRequestBody.getOperationId();
        Optional<Transaction> transaction = moneyTransferRepo.getTransaction(operationId);
        TransactionStatus transactionStatus = TransactionStatus.ERROR;
        String errorMessage = "";

        if (transaction.isPresent()) {
            TransactionStatus cusStatus = transaction.get().getStatus();
            switch (cusStatus) {
                case SUCCESSFULED -> errorMessage = String.format("Транзакция %s подтверждена ранее", operationId);
                case CANCELED -> errorMessage = String.format("Транзакция %s отменена ранее", operationId);
                case NEW -> {
                    if (confirmRequestBody.getCode() == null || confirmRequestBody.getCode().isEmpty()) {
                        errorMessage = String.format("Транзакция %s отменена", operationId);
                        transactionStatus = TransactionStatus.CANCELED;
                    } else {
                        if (validateData.checkConfirmCode(transaction.get(), confirmRequestBody.getCode())) {
                            errorMessage = String.format("Транзакция %s подтверждена", operationId);
                            transactionStatus = TransactionStatus.SUCCESSFULED;
                        } else {
                            errorMessage = String.format("Для транзакции %s не верно указан код подтверждения", operationId);
                        }
                    }
                }
            }
        } else {
            errorMessage = String.format("Транзакция %s не найдена", operationId);
        }
        addStringInLog(operationId, String.valueOf(transactionStatus), errorMessage);
        if (transactionStatus.equals(TransactionStatus.ERROR)) {
            throw new ConfirmException(errorMessage);
        } else {
            transaction.get().setStatus(transactionStatus);
        }
        return new MoneyTransferResponse(confirmRequestBody.getOperationId());
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