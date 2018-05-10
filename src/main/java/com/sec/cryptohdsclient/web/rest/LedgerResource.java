package com.sec.cryptohdsclient.web.rest;

import com.sec.cryptohdsclient.EnvelopeHandler;
import com.sec.cryptohdsclient.web.rest.exceptions.CryptohdsRestException;
import com.sec.cryptohdslibrary.envelope.Envelope;
import com.sec.cryptohdslibrary.keystore.KeyStoreImpl;
import com.sec.cryptohdslibrary.service.dto.LedgerBalanceDTO;
import com.sec.cryptohdslibrary.service.dto.LedgerDTO;
import com.sec.cryptohdslibrary.service.dto.OperationDTO;
import com.sec.cryptohdslibrary.service.dto.OperationListDTO;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LedgerResource extends CryptohdsResource {

    private final EnvelopeHandler envelopeHandler;

    public LedgerResource(EnvelopeHandler envelopeHandler) {
        this.envelopeHandler = envelopeHandler;
    }

    public int updateLedgerSeqNumber(HashMap<String, Envelope> envelopes, String publicKey, KeyStoreImpl keyStore, int localSequenceNumber) throws CryptohdsRestException {
        List<ResponseEntity<Envelope>> results = secureRequests(envelopes, "/ledger/update", publicKey);

        LedgerDTO ledgerDTO = (LedgerDTO) this.envelopeHandler.handleIncomeEnvelopes(keyStore, results, localSequenceNumber);

        return ledgerDTO.getSeqNumber();
    }

    public boolean createLedger(HashMap<String, Envelope> envelopes, String publicKey) throws CryptohdsRestException {
        List<ResponseEntity<Envelope>> results = secureRequests(envelopes, "ledgers", publicKey);

//        return result.getStatusCode() == HttpStatus.NO_CONTENT;
        return true;
    }

    public boolean checkBalance(HashMap<String, Envelope> envelopes, String publicKey, KeyStoreImpl keyStore, int localSequenceNumber) throws CryptohdsRestException {
        List<ResponseEntity<Envelope>> results = secureRequests(envelopes, "ledger/balance", publicKey);

        LedgerBalanceDTO ledgerBalanceDTO = (LedgerBalanceDTO) this.envelopeHandler.handleIncomeEnvelopes(keyStore, results, localSequenceNumber);

        System.out.println(ledgerBalanceDTO.getBalance());
        for(OperationDTO op : ledgerBalanceDTO.getPendingOperations()) {
            System.out.println(op);
        }

//        return result.getStatusCode() == HttpStatus.NO_CONTENT;
        return true;
    }

    public boolean audit(HashMap<String, Envelope> envelopes, String publicKey, KeyStoreImpl keyStore, int localSequenceNumber) throws CryptohdsRestException{
        List<ResponseEntity<Envelope>> results = secureRequests(envelopes, "ledger/audit", publicKey);
        OperationListDTO operationListDTO = (OperationListDTO) this.envelopeHandler.handleIncomeEnvelopes(keyStore, results, localSequenceNumber);

        for(OperationDTO op : operationListDTO.getPendingOperations()) {
            System.out.println(op);
        }

//        return result.getStatusCode() == HttpStatus.NO_CONTENT;
        return true;
    }
}
