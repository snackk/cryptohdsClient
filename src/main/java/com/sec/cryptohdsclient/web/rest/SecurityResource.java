package com.sec.cryptohdsclient.web.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SecurityResource {

    @Value("${rest.ip}")
    private static String restIp;

    @Value("${rest.port}")
    private static int restPort;

    private static final String URL = "http://localhost:8080/api/";

    public String getPublicKey() {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(URL + "security/keys", String.class);
    }
}
