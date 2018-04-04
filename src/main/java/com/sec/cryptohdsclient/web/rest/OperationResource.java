package com.sec.cryptohdsclient.web.rest;

import com.sec.cryptohdslibrary.envelope.Envelope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OperationResource extends CryptohdsResource {

    public boolean sendOperation(Envelope envelope, String publicKey) {
        ResponseEntity<Envelope> result = secureRequest(envelope, "operation/send", publicKey);

        return result.getStatusCode() == HttpStatus.NO_CONTENT;
    }

    public boolean receiveOperation(Envelope envelope, String publicKey) {
        ResponseEntity<Envelope> result = secureRequest(envelope, "operation/receive", publicKey);

        return result.getStatusCode() == HttpStatus.NO_CONTENT;
    }
}
