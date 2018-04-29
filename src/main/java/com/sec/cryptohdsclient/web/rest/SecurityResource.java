package com.sec.cryptohdsclient.web.rest;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SecurityResource {

    public String URL;

    private final CryptohdsInstances cryptohdsInstances;

    public SecurityResource(CryptohdsInstances cryptohdsInstances) {
        this.cryptohdsInstances = cryptohdsInstances;

        this.URL = cryptohdsInstances.getUrls().get(0);
    }

    public String getPublicKey() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(URL + "security/keys", String.class);
    }
}
