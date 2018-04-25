package com.sec.cryptohdsclient.web.rest;

import com.sec.cryptohdsclient.web.rest.exceptions.CryptohdsRestException;
import com.sec.cryptohdslibrary.envelope.Envelope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OperationResource extends CryptohdsResource {

    private final Environment environment;

    public OperationResource(Environment environment) {
        this.environment = environment;

        this.restPort = Integer.parseInt(this.environment.getProperty("rest.port"));
        this.restIp = this.environment.getProperty("rest.ip");
        this.URL = "http://" + restIp + ":" + restPort + "/api/";
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
