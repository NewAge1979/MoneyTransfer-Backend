package pl.chrapatij.moneytransfer.model;

import lombok.Data;

@Data
public class MoneyTransferError {
    private final String message;
    private final int id;
}