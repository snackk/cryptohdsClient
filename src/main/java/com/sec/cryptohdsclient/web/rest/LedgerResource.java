package com.sec.cryptohdsclient.web.rest;

import com.sec.cryptohdsclient.EnvelopeHandler;
import com.sec.cryptohdsclient.web.rest.exceptions.CryptohdsRestException;
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
        try {
            ResponseEntity<Envelope> result = secureRequest(envelope, "ledgers", publicKey);
            return result.getStatusCode() == HttpStatus.NO_CONTENT;

        } catch (CryptohdsRestException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean checkBalance(Envelope envelope, String publicKey, KeyStoreImpl keyStore) throws IOException, ClassNotFoundException {
        try {
            ResponseEntity<Envelope> result = secureRequest(envelope, "ledger/balance", publicKey);
            LedgerBalanceDTO ledgerBalanceDTO = (LedgerBalanceDTO) this.envelopeHandler.handleIncomeEnvelope(keyStore, result.getBody());

            System.out.println(ledgerBalanceDTO.getBalance());
            for(OperationDTO op : ledgerBalanceDTO.getPendingOperations()) {
                System.out.println(op);
            }

            return result.getStatusCode() == HttpStatus.OK;

        } catch (CryptohdsRestException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean audit(Envelope envelope, String publicKey, KeyStoreImpl keyStore) throws IOException, ClassNotFoundException {
        try {
            ResponseEntity<Envelope> result = secureRequest(envelope, "ledger/audit", publicKey);
            OperationListDTO operationListDTO = (OperationListDTO) this.envelopeHandler.handleIncomeEnvelope(keyStore, result.getBody());

            for(OperationDTO op : operationListDTO.getPendingOperations()) {
                System.out.println(op);
            }

            return result.getStatusCode() == HttpStatus.OK;

        } catch (CryptohdsRestException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
