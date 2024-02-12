package pl.chrapatij.moneytransfer.service;

import pl.chrapatij.moneytransfer.model.ConfirmRequestBody;
import pl.chrapatij.moneytransfer.model.MoneyTransferResponse;
import pl.chrapatij.moneytransfer.model.TransferRequestBody;

public interface MoneyTransferSrv {
    MoneyTransferResponse transfer(TransferRequestBody transferRequestBody);

    MoneyTransferResponse confirm(ConfirmRequestBody confirmRequestBody);
}