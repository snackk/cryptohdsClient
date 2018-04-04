package com.sec.cryptohdsclient.web.rest;

import com.sec.cryptohdsclient.EnvelopeHandler;
import com.sec.cryptohdslibrary.envelope.Envelope;
import com.sec.cryptohdslibrary.keystore.KeyStoreImpl;
import com.sec.cryptohdslibrary.service.dto.LedgerBalanceDTO;
import com.sec.cryptohdslibrary.service.dto.OperationDTO;
import com.sec.cryptohdslibrary.service.dto.OperationListDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class LedgerResource extends CryptohdsResource {

    private final EnvelopeHandler envelopeHandler;

    public LedgerResource(EnvelopeHandler envelopeHandler) {
        this.envelopeHandler = envelopeHandler;
    }

    public boolean createLedger(Envelope envelope, String publicKey) {
        ResponseEntity<Envelope> result = secureRequest(envelope, "ledgers", publicKey);

        return result.getStatusCode() == HttpStatus.NO_CONTENT;
    }

    public boolean checkBalance(Envelope envelope, String publicKey, KeyStoreImpl keyStore) throws IOException, ClassNotFoundException {
        ResponseEntity<Envelope> result = secureRequest(envelope, "ledger/balance", publicKey);
        LedgerBalanceDTO ledgerBalanceDTO = (LedgerBalanceDTO) this.envelopeHandler.handleIncomeEnvelope(keyStore, result.getBody());

        System.out.println(ledgerBalanceDTO.getBalance());

        return result.getStatusCode() == HttpStatus.OK;
    }

    public boolean audit(Envelope envelope, String publicKey, KeyStoreImpl keyStore) throws IOException, ClassNotFoundException {
        ResponseEntity<Envelope> result = secureRequest(envelope, "ledger/audit", publicKey);
        OperationListDTO operationListDTO = (OperationListDTO) this.envelopeHandler.handleIncomeEnvelope(keyStore, result.getBody());

        for(OperationDTO op : operationListDTO.getPendingOperations()) {
            System.out.println(op);
        }

        return result.getStatusCode() == HttpStatus.OK;
    }
}
