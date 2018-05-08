package com.sec.cryptohdsclient.web.rest;

import com.sec.cryptohdsclient.web.rest.errors.CustomRestExceptionHandler;
import com.sec.cryptohdslibrary.envelope.Envelope;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.springframework.core.task.support.ConcurrentExecutorAdapter;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public abstract class CryptohdsResource {    
    
	
	
	
    protected final ResponseEntity<Envelope> secureRequest(HashMap<String, Envelope> envelopes, String endpoint, String publicKey) {
    	int numServers = envelopes.size();
		final int failures = (numServers - 1)/3;
    	final CountDownLatch l = new CountDownLatch(2 * failures + 1);
    	final ConcurrentHashMap<String, ResponseEntity<Envelope>> responseHashMap = new ConcurrentHashMap<>();
    	
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
		return responseHashMap.get(0);
        
  
    }
    

    
}
