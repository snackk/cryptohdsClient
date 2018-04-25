package com.sec.cryptohdsclient.web.rest;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SecurityResource {

    private String restIp;

    private int restPort;

    public String URL;

    private final Environment environment;

    public SecurityResource(Environment environment) {
        this.environment = environment;

        this.restPort = Integer.parseInt(this.environment.getProperty("rest.port"));
        this.restIp = this.environment.getProperty("rest.ip");
        this.URL = "http://" + restIp + ":" + restPort + "/api/";
    }

    public String getPublicKey() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(URL + "security/keys", String.class);
    }
}
