package com.sec.cryptohdsclient;

import com.sec.cryptohdsclient.web.rest.exceptions.CryptohdsRestException;
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

    public CryptohdsDTO handleIncomeEnvelope(KeyStoreImpl clientKeyStore, Envelope envelope, int localSequenceNumber) throws CryptohdsRestException {
        Message message = null;
        try {
            message = envelope.decipherEnvelope(clientKeyStore);

        } catch(ClassNotFoundException | IOException e) {
            throw new CryptohdsRestException("Error on deciphering Envelope!");
        }

        if (localSequenceNumber != -1 && message != null && (message.getSeqNumber() != localSequenceNumber + 1)) {
            throw new CryptohdsRestException("Ledger sequence number doesn't match with server's!");
        }

        if (message != null &&  !message.verifyMessageSignature(envelope.getClientPublicKey())) {
            throw new CryptohdsRestException("Envelope validation failed!");
        }

        if (message != null)
            return message.getContent();
        else return null;
    }
}
