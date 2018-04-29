package com.sec.cryptohdsclient.web.rest;

import com.sec.cryptohdsclient.web.rest.exceptions.CryptohdsRestException;
import com.sec.cryptohdslibrary.envelope.Envelope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OperationResource extends CryptohdsResource {

    private final CryptohdsInstances cryptohdsInstances;

    public OperationResource(CryptohdsInstances cryptohdsInstances) {
        this.cryptohdsInstances = cryptohdsInstances;

        this.URL = cryptohdsInstances.getUrls().get(0);
    }

    public boolean sendOperation(Envelope envelope, String publicKey) throws CryptohdsRestException {
        ResponseEntity<Envelope> result = secureRequest(envelope, "operation/send", publicKey);

        return result.getStatusCode() == HttpStatus.NO_CONTENT;
    }

    public boolean receiveOperation(Envelope envelope, String publicKey) throws CryptohdsRestException {
        ResponseEntity<Envelope> result = secureRequest(envelope, "operation/receive", publicKey);

        return result.getStatusCode() == HttpStatus.NO_CONTENT;
    }
}
