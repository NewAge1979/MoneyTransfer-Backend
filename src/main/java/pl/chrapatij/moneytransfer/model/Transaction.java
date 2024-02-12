package pl.chrapatij.moneytransfer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Transaction {
    private String operationId;
    private String confirmCode;
    private TransactionStatus status;
}