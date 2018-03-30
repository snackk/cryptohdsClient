package com.sec.cryptohdsClient.handler;

import com.sec.cryptohdslibrary.keystore.KeyStoreImpl;

public class ClientHandler {

    private KeyStoreImpl clientKeyStore;

    private KeyStoreImpl getKeyStore() {
        return clientKeyStore;
    }

    public void register(String ledgerName, String ledgerPassword) {
        this.clientKeyStore = new KeyStoreImpl(ledgerName, ledgerPassword);
    }
}
