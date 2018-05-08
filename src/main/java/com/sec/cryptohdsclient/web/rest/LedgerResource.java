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

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LedgerResource extends CryptohdsResource {

    private final EnvelopeHandler envelopeHandler;



    public LedgerResource(EnvelopeHandler envelopeHandler) {
        this.envelopeHandler = envelopeHandler;
        
       
    }

    public int updateLedgerSeqNumber(HashMap<String, Envelope> envelopes, String publicKey, KeyStoreImpl keyStore, int localSequenceNumber) throws CryptohdsRestException {
        ResponseEntity<Envelope> result = secureRequest(envelopes, "/ledger/update", publicKey);

        LedgerDTO ledgerDTO = (LedgerDTO) this.envelopeHandler.handleIncomeEnvelope(keyStore, result.getBody(), localSequenceNumber);

        return ledgerDTO.getSeqNumber();
    }

    public boolean createLedger(HashMap<String, Envelope> envelopes, String publicKey) throws CryptohdsRestException {
        ResponseEntity<Envelope> result = secureRequest(envelopes, "ledgers", publicKey);

        return result.getStatusCode() == HttpStatus.NO_CONTENT;
    }

    public boolean checkBalance(HashMap<String, Envelope> envelopes, String publicKey, KeyStoreImpl keyStore, int localSequenceNumber) throws CryptohdsRestException {
        ResponseEntity<Envelope> result = secureRequest(envelopes, "ledger/balance", publicKey);
        LedgerBalanceDTO ledgerBalanceDTO = (LedgerBalanceDTO) this.envelopeHandler.handleIncomeEnvelope(keyStore, result.getBody(), localSequenceNumber);

        System.out.println(ledgerBalanceDTO.getBalance());
        for(OperationDTO op : ledgerBalanceDTO.getPendingOperations()) {
            System.out.println(op);
        }

        return result.getStatusCode() == HttpStatus.OK;
    }

    public boolean audit(HashMap<String, Envelope> envelopes, String publicKey, KeyStoreImpl keyStore, int localSequenceNumber) throws CryptohdsRestException{
        ResponseEntity<Envelope> result = secureRequest(envelopes, "ledger/audit", publicKey);
        OperationListDTO operationListDTO = (OperationListDTO) this.envelopeHandler.handleIncomeEnvelope(keyStore, result.getBody(), localSequenceNumber);

        for(OperationDTO op : operationListDTO.getPendingOperations()) {
            System.out.println(op);
        }

        return result.getStatusCode() == HttpStatus.OK;
    }
}
