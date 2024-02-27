package pl.chrapatij.moneytransfer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<MoneyTransferResponse> transfer(@RequestBody @Valid TransferRequestBody transferRequestBody) {
        return new ResponseEntity<>(moneyTransferSrv.transfer(transferRequestBody), HttpStatus.OK);
    }

    @PostMapping("/confirmOperation")
    public ResponseEntity<MoneyTransferResponse> confirm(@RequestBody @Valid ConfirmRequestBody confirmRequestBody) {
        return new ResponseEntity<>(moneyTransferSrv.confirm(confirmRequestBody), HttpStatus.OK);
    }
}