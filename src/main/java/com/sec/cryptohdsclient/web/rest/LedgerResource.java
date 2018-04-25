package com.sec.cryptohdsclient.web.rest;

import com.sec.cryptohdsclient.EnvelopeHandler;
import com.sec.cryptohdsclient.web.rest.exceptions.CryptohdsRestException;
import com.sec.cryptohdslibrary.envelope.Envelope;
import com.sec.cryptohdslibrary.keystore.KeyStoreImpl;
import com.sec.cryptohdslibrary.service.dto.LedgerBalanceDTO;
import com.sec.cryptohdslibrary.service.dto.LedgerDTO;
import com.sec.cryptohdslibrary.service.dto.OperationDTO;
import com.sec.cryptohdslibrary.service.dto.OperationListDTO;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LedgerResource extends CryptohdsResource {

    private final EnvelopeHandler envelopeHandler;

    private final Environment environment;

    public LedgerResource(EnvelopeHandler envelopeHandler, Environment environment) {
        this.envelopeHandler = envelopeHandler;
        this.environment = environment;

        this.restPort = Integer.parseInt(this.environment.getProperty("rest.port"));
        this.restIp = this.environment.getProperty("rest.ip");
        this.URL = "http://" + restIp + ":" + restPort + "/api/";
    }

    public int updateLedgerSeqNumber(Envelope envelope, String publicKey, KeyStoreImpl keyStore, int localSequenceNumber) throws CryptohdsRestException {
        ResponseEntity<Envelope> result = secureRequest(envelope, "/ledger/update", publicKey);

        LedgerDTO ledgerDTO = (LedgerDTO) this.envelopeHandler.handleIncomeEnvelope(keyStore, result.getBody(), localSequenceNumber);

        return ledgerDTO.getSeqNumber();
    }

    public boolean createLedger(Envelope envelope, String publicKey) throws CryptohdsRestException {
        ResponseEntity<Envelope> result = secureRequest(envelope, "ledgers", publicKey);

        return result.getStatusCode() == HttpStatus.NO_CONTENT;
    }

    public boolean checkBalance(Envelope envelope, String publicKey, KeyStoreImpl keyStore, int localSequenceNumber) throws CryptohdsRestException {
        ResponseEntity<Envelope> result = secureRequest(envelope, "ledger/balance", publicKey);
        LedgerBalanceDTO ledgerBalanceDTO = (LedgerBalanceDTO) this.envelopeHandler.handleIncomeEnvelope(keyStore, result.getBody(), localSequenceNumber);

        System.out.println(ledgerBalanceDTO.getBalance());
        for(OperationDTO op : ledgerBalanceDTO.getPendingOperations()) {
            System.out.println(op);
        }

        return result.getStatusCode() == HttpStatus.OK;
    }

    public boolean audit(Envelope envelope, String publicKey, KeyStoreImpl keyStore, int localSequenceNumber) throws CryptohdsRestException{
        ResponseEntity<Envelope> result = secureRequest(envelope, "ledger/audit", publicKey);
        OperationListDTO operationListDTO = (OperationListDTO) this.envelopeHandler.handleIncomeEnvelope(keyStore, result.getBody(), localSequenceNumber);

        for(OperationDTO op : operationListDTO.getPendingOperations()) {
            System.out.println(op);
        }

        return result.getStatusCode() == HttpStatus.OK;
    }
}
