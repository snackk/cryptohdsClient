package com.sec.cryptohdsclient.handler;

import com.sec.cryptohdslibrary.keystore.ClientKeyStore;

public class ClientHandler {

    private ClientKeyStore clientKeyStore;

    private ClientKeyStore getKeyStore() {
        return clientKeyStore;
    }

    public ClientHandler() {
        this.clientKeyStore = new ClientKeyStore();
    }

    public void register(String ledgerName, String ledgerPassword) {
        this.getKeyStore().generateKeyStore(ledgerName, ledgerPassword);
    }
}
