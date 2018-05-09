package com.sec.cryptohdsclient.web.rest;

import com.sec.cryptohdsclient.web.rest.errors.CustomRestExceptionHandler;
import com.sec.cryptohdslibrary.envelope.Envelope;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class CryptohdsResource {    
    
	
	
	
    protected final ResponseEntity<Envelope> secureRequest(HashMap<String, Envelope> envelopes, String endpoint, String publicKey) {
    	int numServers = envelopes.size();
		final int failures = (numServers - 1)/3;
    	final CountDownLatch l = new CountDownLatch(2 * failures + 1);
    	ConcurrentHashMap<String, ResponseEntity<Envelope>> responseHashMap = new ConcurrentHashMap<>();
    	
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomRestExceptionHandler());
        for(String ip : envelopes.keySet()) {
        	Envelope envelope = envelopes.get(ip);
        	envelope.setClientPublicKey(publicKey);

        	Thread t = new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						HttpEntity<?> request = new HttpEntity<Object>(envelope);
				        
				        responseHashMap.put(ip, restTemplate.postForEntity(ip + endpoint, request, Envelope.class));
					}
					catch(Exception e){
					}
					finally{
						l.countDown();						
					}
				}
        	});
        	t.start();       	
        }
        try {
        	l.await();
        }
        catch(InterruptedException e){
       
		}
		//TODO Review this shitty code WTF.
		// Also, result.headers[0].value[0] -> contains Wed, 09 May 2018 00:27:58 GMT.  Timestamp criado no servidor
		return responseHashMap.entrySet().iterator().next().getValue();
    }
}
