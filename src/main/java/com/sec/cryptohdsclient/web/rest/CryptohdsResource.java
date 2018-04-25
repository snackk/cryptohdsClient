package com.sec.cryptohdsclient.web.rest;

import com.sec.cryptohdsclient.web.rest.errors.CustomRestExceptionHandler;
import com.sec.cryptohdslibrary.envelope.Envelope;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class CryptohdsResource {

    protected String restIp;

    protected int restPort;

    public String URL;

    protected final ResponseEntity<Envelope> secureRequest(Envelope envelope, String endpoint, String publicKey) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomRestExceptionHandler());
        envelope.setClientPublicKey(publicKey);

        HttpEntity<?> request = new HttpEntity<Object>(envelope);

        return restTemplate.postForEntity(URL + endpoint, request, Envelope.class);
    }
}
