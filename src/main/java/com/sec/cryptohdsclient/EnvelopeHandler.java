package com.sec.cryptohdsclient;

import com.sec.cryptohdsclient.web.rest.exceptions.CryptohdsRestException;
import com.sec.cryptohdslibrary.envelope.Envelope;
import com.sec.cryptohdslibrary.envelope.Message;
import com.sec.cryptohdslibrary.keystore.KeyStoreImpl;
import com.sec.cryptohdslibrary.service.dto.CryptohdsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class EnvelopeHandler {

    public EnvelopeHandler() {

    }

    public CryptohdsDTO handleIncomeEnvelopes(KeyStoreImpl clientKeyStore, HashMap<String, ResponseEntity<Envelope>> envelopes, Map<String, Integer> localSequenceNumbers) throws CryptohdsRestException {

        HashMap<String, Message> messages = getEnvelopeMessages(clientKeyStore, envelopes, localSequenceNumbers);

        HashMap<Integer, Integer> messageQuorum = new HashMap<>();
        messages.entrySet().stream().forEach(k -> messageQuorum.put(k.getValue().getSeqNumber(), 0));

        Message storedMessage = null;
        for(String ip: messages.keySet()) {
            localSequenceNumbers.put(ip, localSequenceNumbers.get(ip) + 1);

            messageQuorum.put(localSequenceNumbers.get(ip),messageQuorum.get(localSequenceNumbers.get(ip)) + 1);
        }

        Integer maxFreq = 0;
        Integer maxSeqNumber = 0;
        for(Integer seqNumber : messageQuorum.keySet()) {
            Integer frequency = messageQuorum.get(seqNumber);
            if (frequency > maxFreq) {
                maxFreq = frequency;
                maxSeqNumber = seqNumber;
            }
            if(frequency == maxFreq && seqNumber > maxSeqNumber) {
                maxFreq = frequency;
                maxSeqNumber = seqNumber;
            }
        }

        for(Message message : messages.values()){
            if(message.getSeqNumber() == maxSeqNumber){
                storedMessage = message;
            }
        }

        if (storedMessage != null)
            return storedMessage.getContent();
        else return null;
    }

    public void updateSeqNumberFromEnvelopes(KeyStoreImpl clientKeyStore, HashMap<String, ResponseEntity<Envelope>> envelopes, Map<String, Integer> localSequenceNumbers) throws CryptohdsRestException {
        HashMap<String, Message> messages = getEnvelopeMessages(clientKeyStore, envelopes, localSequenceNumbers);

        for(String ip : messages.keySet()) {
            localSequenceNumbers.put(ip, messages.get(ip).getSeqNumber());
        }
    }

    private HashMap<String, Message> getEnvelopeMessages(KeyStoreImpl clientKeyStore, HashMap<String, ResponseEntity<Envelope>> envelopes, Map<String, Integer> localSequenceNumbers) throws CryptohdsRestException {
        HashMap<String, Message> messages = new HashMap<>();

        for(String ip : envelopes.keySet()) {
            ResponseEntity<Envelope> envelope = envelopes.get(ip);

            Message temp = null;
            try {
                temp = envelope.getBody().decipherEnvelope(clientKeyStore);

            } catch(ClassNotFoundException | IOException e) {
                throw new CryptohdsRestException("Error on deciphering Envelope!");
            }

            messages.put(ip, temp);

            if (localSequenceNumbers.get(ip) != -1 && temp != null && (temp.getSeqNumber() != localSequenceNumbers.get(ip) + 1)) {
                throw new CryptohdsRestException("Ledger sequence number doesn't match with server's!");
            }

            if (temp != null &&  !temp.verifyMessageSignature(envelope.getBody().getClientPublicKey())) {
                throw new CryptohdsRestException("Envelope validation failed!");
            }
        }

        return messages;
    }
}
