package com.sec.cryptohdsclient;

import com.sec.cryptohdslibrary.envelope.Envelope;
import com.sec.cryptohdslibrary.envelope.Message;
import com.sec.cryptohdslibrary.keystore.KeyStoreImpl;
import com.sec.cryptohdslibrary.service.dto.CryptohdsDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EnvelopeHandler {

    public EnvelopeHandler() {

    }

    public CryptohdsDTO handleIncomeEnvelope(KeyStoreImpl clientKeyStore, Envelope envelope) throws IOException, ClassNotFoundException {
        Message message = envelope.decipherEnvelope(clientKeyStore);
        if (!message.verifyMessageSignature(envelope.getClientPublicKey())) {
            System.out.println("Envelope validation failed!");
        }
        return message.getContent();
    }
}
