package com.sec.cryptohdsclient.handler;

import com.sec.cryptohdsclient.web.rest.LedgerResource;
import com.sec.cryptohdsclient.web.rest.SecurityResource;
import com.sec.cryptohdslibrary.envelope.Envelope;
import com.sec.cryptohdslibrary.envelope.Message;
import com.sec.cryptohdslibrary.keystore.KeyStoreImpl;

import com.sec.cryptohdslibrary.security.CipherInstance;
import com.sec.cryptohdslibrary.service.dto.LedgerDTO;
import org.springframework.stereotype.Service;


@Service
public class ClientHandler {

    private KeyStoreImpl clientKeyStore;

    private final LedgerResource ledgerResource;

    private final SecurityResource securityResource;

    private String cryptoServerPublicKey;

    public ClientHandler(LedgerResource ledgerResource, SecurityResource securityResource) {
        this.ledgerResource = ledgerResource;
        this.securityResource = securityResource;

        this.cryptoServerPublicKey = this.securityResource.getPublicKey();
    }

    private KeyStoreImpl getKeyStore() {
        return clientKeyStore;
    }

    public void register(String ledgerName, String ledgerPassword) {
        this.clientKeyStore = new KeyStoreImpl(ledgerName, ledgerPassword);

        String publicKey = CipherInstance.encodePublicKey(clientKeyStore.getkeyPairHDS().getPublic());

        /*Create LedgerDTO with his public key and encode as base64*/
        LedgerDTO ledgerDTO = new LedgerDTO(ledgerName, publicKey);

        Message message = new Message(ledgerDTO, this.clientKeyStore);
        Envelope envelope = new Envelope();
        envelope.cipherEnvelope(message, cryptoServerPublicKey);

        //TODO O server nao ta preparado para receber isto. yet.
        this.ledgerResource.createLedger(envelope, publicKey);
    }
}
