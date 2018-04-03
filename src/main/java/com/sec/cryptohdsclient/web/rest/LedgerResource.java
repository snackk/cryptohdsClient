package com.sec.cryptohdsclient.web.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    public boolean createLedger(String envelope) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestBody = new HttpEntity<>(envelope);

        ResponseEntity<String> result = restTemplate.postForEntity(URL + "ledgers", requestBody, String.class);

        System.out.println("Status code:" + result.getStatusCode());

        return result.getStatusCode() == HttpStatus.NO_CONTENT;
    }
}
