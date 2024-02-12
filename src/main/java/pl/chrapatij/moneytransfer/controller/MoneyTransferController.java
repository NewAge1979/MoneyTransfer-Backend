package pl.chrapatij.moneytransfer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.chrapatij.moneytransfer.model.ConfirmRequestBody;
import pl.chrapatij.moneytransfer.model.MoneyTransferResponse;
import pl.chrapatij.moneytransfer.model.TransferRequestBody;
import pl.chrapatij.moneytransfer.service.MoneyTransferSrv;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cross.origin}")
public class MoneyTransferController {
    private final MoneyTransferSrv moneyTransferSrv;
    @PostMapping("/transfer")
    public MoneyTransferResponse transfer(@RequestBody @Valid TransferRequestBody transferRequestBody) {
        System.out.println(transferRequestBody.getAmount());
        return moneyTransferSrv.transfer(transferRequestBody);
    }

    @PostMapping("/confirmOperation")
    public MoneyTransferResponse confirm(@RequestBody @Valid ConfirmRequestBody confirmRequestBody) {
        return moneyTransferSrv.confirm(confirmRequestBody);
    }
}