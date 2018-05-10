package com.sec.cryptohdsclient.web.rest;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class SecurityResource {

	private List<String> ips;

    private final CryptohdsInstances cryptohdsInstances;

    public SecurityResource(CryptohdsInstances cryptohdsInstances) {
        this.cryptohdsInstances = cryptohdsInstances;
        this.ips = cryptohdsInstances.getUrls();
    }
    
    public HashMap<String,String> getPublicKey() {
        RestTemplate restTemplate = new RestTemplate();
        HashMap<String,String> ipKeysMap = new HashMap<>();
        for(String ip : ips) {//InvocationTargetException
            try {
                ipKeysMap.put(ip, restTemplate.getForObject(ip + "security/keys", String.class));
            } catch (RestClientException e) {
                // Communication failure with ip
            }
        }
        return ipKeysMap;
    }
}
