package com.sec.cryptohdsclient.web.rest;

import com.sec.cryptohdsclient.web.rest.errors.CustomRestExceptionHandler;
import com.sec.cryptohdsclient.web.rest.exceptions.CryptohdsRestException;
import com.sec.cryptohdslibrary.envelope.Envelope;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public abstract class CryptohdsResource {

	private int failures = 0;
	private CryptohdsRestException exception = null;

    protected final ResponseEntity<Envelope> secureRequest(HashMap<String, Envelope> envelopes, String endpoint, String publicKey) throws CryptohdsRestException{
    	this.failures = 0;
		this.exception = null;

    	int numServers = envelopes.size();
		final int toleratedFailures = (numServers - 1)/3;

    	final CountDownLatch l = new CountDownLatch(2 * toleratedFailures + 1);
    	ConcurrentHashMap<String, ResponseEntity<Envelope>> responseHashMap = new ConcurrentHashMap<>();
    	
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomRestExceptionHandler());

        for(String ip : envelopes.keySet()) {
        	Envelope envelope = envelopes.get(ip);
        	envelope.setClientPublicKey(publicKey);

			Thread t = new Thread(() -> {
				try {
					HttpEntity<?> request = new HttpEntity<Object>(envelope);

					responseHashMap.put(ip, restTemplate.postForEntity(ip + endpoint, request, Envelope.class));

				} catch(CryptohdsRestException e) {
					if(e.getMessage().contains("already")) {
						synchronized (this) {
							this.exception = e;
						}
					}

				} catch(RestClientException e){
					// Caught if byzantine crash
					synchronized (this) {
						this.failures++;
					}
				} finally {
					l.countDown();
				}
			});

			t.start();
        }
        try {
        	l.await();

        } catch(InterruptedException e){
       
		}

		if (exception != null) {
        	throw exception;
		}
		
		if(failures > toleratedFailures) {
			throw new CryptohdsRestException("Not enough servers(" + (numServers - failures) + "/" + numServers + ") to process your request.");
		} else {
        	return responseHashMap.entrySet().iterator().next().getValue();
        }
    }
}
