package com.sec.cryptohdsclient.web.rest;

import com.sec.cryptohdsclient.web.rest.exceptions.CryptohdsRestException;
import com.sec.cryptohdslibrary.envelope.Envelope;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OperationResource extends CryptohdsResource {

    public OperationResource() {
    }

    public boolean sendOperation(HashMap<String, Envelope> envelopes, String publicKey) throws CryptohdsRestException {
        ResponseEntity<Envelope> result = secureRequest(envelopes, "operation/send", publicKey);

        return result.getStatusCode() == HttpStatus.NO_CONTENT;
    }

    public boolean receiveOperation(HashMap<String, Envelope> envelopes, String publicKey) throws CryptohdsRestException {
        ResponseEntity<Envelope> result = secureRequest(envelopes, "operation/receive", publicKey);

        return result.getStatusCode() == HttpStatus.NO_CONTENT;
    }
}
