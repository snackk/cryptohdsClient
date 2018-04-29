package com.sec.cryptohdsclient.web.rest;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CryptohdsInstances {

    private List<String> ips = new ArrayList<>();

    private int port;

    private final Environment environment;

    public CryptohdsInstances(Environment environment) {
        this.environment = environment;

        this.port = Integer.parseInt(this.environment.getProperty("rest.port"));

        String[] splitRes = this.environment.getProperty("rest.ip").split(",");
        for(int i = 0; i < splitRes.length; i++) {
            ips.add(splitRes[i]);
        }
    }

    public List<String> getUrls() {
        List<String> urls = new ArrayList<>();
        for(String ip : ips) {
            urls.add("http://" + ip + ":" + this.port + "/api/");
        }
        return urls;
    }
}
