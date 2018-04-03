package com.sec.cryptohdsclient.handler;

import com.sec.cryptohdsclient.web.rest.LedgerResource;
import com.sec.cryptohdsclient.web.rest.SecurityResource;
import com.sec.cryptohdslibrary.envelope.Envelope;
import com.sec.cryptohdslibrary.keystore.KeyStoreImpl;

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

        Envelope envelope = new Envelope(ledgerName, this.clientKeyStore);
        System.out.println(envelope.getCipheredEnvelope(cryptoServerPublicKey));
        this.ledgerResource.createLedger(envelope.getCipheredEnvelope(cryptoServerPublicKey));
    }
}
