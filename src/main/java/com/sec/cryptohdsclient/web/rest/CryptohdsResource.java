package com.sec.cryptohdsclient.web.rest;

import com.sec.cryptohdsclient.web.rest.errors.CustomRestExceptionHandler;
import com.sec.cryptohdslibrary.envelope.Envelope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class CryptohdsResource {

    @Value("${rest.ip}")
    private static String restIp;

    @Value("${rest.port}")
    private static int restPort;

    private static final String URL = "http://localhost:8080/api/";

    protected final ResponseEntity<Envelope> secureRequest(Envelope envelope, String endpoint, String publicKey) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomRestExceptionHandler());
        envelope.setClientPublicKey(publicKey);

        HttpEntity<?> request = new HttpEntity<Object>(envelope);

        return restTemplate.postForEntity(URL + endpoint, request, Envelope.class);
    }
}
