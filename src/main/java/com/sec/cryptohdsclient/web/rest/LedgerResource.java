package com.sec.cryptohdsclient.web.rest;

import com.sec.cryptohdslibrary.envelope.Envelope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class LedgerResource {

    @Value("${rest.ip}")
    private static String restIp;

    @Value("${rest.port}")
    private static int restPort;

    private static final String URL = "http://localhost:8080/api/";

/*  Simple REST client, not using Envelope crap
    public boolean createLedger(LedgerDTO ledgerDTO) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<LedgerDTO> requestBody = new HttpEntity<>(ledgerDTO);

        ResponseEntity<LedgerDTO> result = restTemplate.postForEntity(URL + "ledgers", requestBody, LedgerDTO.class);

        System.out.println("Status code:" + result.getStatusCode());

        return result.getStatusCode() == HttpStatus.NO_CONTENT;
    }*/

    public boolean createLedger(Envelope envelope, String publicKey) {
        RestTemplate restTemplate = new RestTemplate();
        envelope.setClientPublicKey(publicKey);

        HttpEntity<?> request = new HttpEntity<Object>(envelope);

        ResponseEntity<Envelope> result = restTemplate.postForEntity(URL + "ledgers", request, Envelope.class);
        System.out.println("Status code:" + result.getStatusCode());

        return result.getStatusCode() == HttpStatus.NO_CONTENT;
    }
}
