package com.sec.cryptohdsclient;

import com.sec.cryptohdsclient.web.rest.exceptions.CryptohdsRestException;
import com.sec.cryptohdslibrary.envelope.Envelope;
import com.sec.cryptohdslibrary.envelope.Message;
import com.sec.cryptohdslibrary.keystore.KeyStoreImpl;
import com.sec.cryptohdslibrary.service.dto.CryptohdsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
public class EnvelopeHandler {

    public EnvelopeHandler() {

    }

    public CryptohdsDTO handleIncomeEnvelopes(KeyStoreImpl clientKeyStore, List<ResponseEntity<Envelope>> envelopes, int localSequenceNumber) throws CryptohdsRestException {
        Message storedMessage = null;
        Envelope storedEnvelope = null;
        HashMap<Message, Envelope> messages = new HashMap<>();

        for(ResponseEntity<Envelope> envelope : envelopes) {
            Message temp = null;
            try {
                temp = envelope.getBody().decipherEnvelope(clientKeyStore);

            } catch(ClassNotFoundException | IOException e) {
                throw new CryptohdsRestException("Error on deciphering Envelope!");
            }
            messages.put(temp, envelope.getBody());
        }

        int maxSeqNumber = 0;
        for(Message message : messages.keySet()) {

            if(message.getSeqNumber() > maxSeqNumber) {
                maxSeqNumber = message.getSeqNumber();
                storedMessage = message;
                storedEnvelope = messages.get(message);
            }
        }

        if (localSequenceNumber != -1 && storedMessage != null && (storedMessage.getSeqNumber() != localSequenceNumber + 1)) {
            throw new CryptohdsRestException("Ledger sequence number doesn't match with server's!");
        }

        if (storedMessage != null &&  !storedMessage.verifyMessageSignature(storedEnvelope.getClientPublicKey())) {
            throw new CryptohdsRestException("Envelope validation failed!");
        }

        if (storedMessage != null)
            return storedMessage.getContent();
        else return null;
    }
}
