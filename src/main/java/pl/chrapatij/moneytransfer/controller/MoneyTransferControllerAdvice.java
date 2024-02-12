package pl.chrapatij.moneytransfer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.chrapatij.moneytransfer.exception.ConfirmException;
import pl.chrapatij.moneytransfer.exception.InputDataException;
import pl.chrapatij.moneytransfer.exception.TransferException;
import pl.chrapatij.moneytransfer.model.MoneyTransferError;

@ControllerAdvice
public class MoneyTransferControllerAdvice {
    @ExceptionHandler(InputDataException.class)
    public ResponseEntity<MoneyTransferError> handleInputData(InputDataException e) {
        return ResponseEntity.badRequest().body(new MoneyTransferError(e.getMessage(), 404));
    }

    @ExceptionHandler(TransferException.class)
    public ResponseEntity<MoneyTransferError> handleTransfer(TransferException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MoneyTransferError(e.getMessage(), 500));
    }

    @ExceptionHandler(ConfirmException.class)
    public ResponseEntity<MoneyTransferError> handleConfirm(ConfirmException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MoneyTransferError(e.getMessage(), 500));
    }
}