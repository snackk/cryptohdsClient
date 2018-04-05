package com.sec.cryptohdsclient.handler;

import com.sec.cryptohdsclient.web.rest.LedgerResource;
import com.sec.cryptohdsclient.web.rest.OperationResource;
import com.sec.cryptohdsclient.web.rest.SecurityResource;
import com.sec.cryptohdsclient.web.rest.exceptions.CryptohdsRestException;
import com.sec.cryptohdslibrary.envelope.Envelope;
import com.sec.cryptohdslibrary.envelope.Message;
import com.sec.cryptohdslibrary.keystore.KeyStoreImpl;

import com.sec.cryptohdslibrary.security.CipherInstance;
import com.sec.cryptohdslibrary.service.dto.CryptohdsDTO;
import com.sec.cryptohdslibrary.service.dto.LedgerDTO;

import java.io.IOException;

import com.sec.cryptohdslibrary.service.dto.OperationDTO;
import com.sec.cryptohdslibrary.service.dto.ReceiveOperationDTO;
import org.springframework.stereotype.Service;


@Service
public class ClientHandler {

    private KeyStoreImpl clientKeyStore;

    private final LedgerResource ledgerResource;

    private final OperationResource operationResource;

    private final SecurityResource securityResource;

    private LedgerDTO ledgerDTO;

    private String cryptoServerPublicKey;

    public ClientHandler(LedgerResource ledgerResource, SecurityResource securityResource, OperationResource operationResource) {
        this.ledgerResource = ledgerResource;
        this.securityResource = securityResource;
        this.operationResource = operationResource;

        this.cryptoServerPublicKey = this.securityResource.getPublicKey();
    }

    private KeyStoreImpl getKeyStore() {
        return clientKeyStore;
    }

    public void ledgerRegister(String ledgerName, String ledgerPassword) {

        /*Initialize KeyStore*/
        this.clientKeyStore = new KeyStoreImpl(ledgerName, ledgerPassword);

        /*Create LedgerDTO with his public key and encode as base64. Also store LedgerDTO*/
        this.ledgerDTO = new LedgerDTO(ledgerName, CipherInstance.encodePublicKey(clientKeyStore.getkeyPairHDS().getPublic()));

        try {
            if (this.ledgerResource.createLedger(handleEnvelope(this.ledgerDTO), this.ledgerDTO.getPublicKey()))
                System.out.println("OK.");
            else System.out.println("NOK.");

        } catch(IOException e) {
            System.out.println("Error while ciphering the Envelope!");

        } catch (CryptohdsRestException e) {
            if(e.getMessage().contains("already")) {
                System.out.println("Using ledger with name: " + ledgerName);
            } else {
                System.out.println("NOK.");
            }
        }
    }

    public void ledgerCheckBalance() {
        if(clientKeyStore == null)
            System.out.println("Register First!");

        try {
            if (this.ledgerResource.checkBalance(handleEnvelope(this.ledgerDTO), this.ledgerDTO.getPublicKey(), clientKeyStore))
                System.out.println("OK.");
            else System.out.println("NOK.");
        } catch(IOException | ClassNotFoundException e) {
            System.out.println("Error while ciphering the Envelope!");
        }
    }

    public void ledgerAudit() {
        if(clientKeyStore == null)
            System.out.println("Register First!");

        try {
            if (this.ledgerResource.audit(handleEnvelope(this.ledgerDTO), this.ledgerDTO.getPublicKey(), clientKeyStore))
                System.out.println("OK.");
            else System.out.println("NOK.");
        } catch(IOException | ClassNotFoundException e) {
            System.out.println("Error while ciphering the Envelope!");
        }
    }

    public void operationSend(Long value, String destinationPublicKey) {
        if(clientKeyStore == null)
            System.out.println("Register First!");

        OperationDTO op = new OperationDTO();
        op.setValue(value);
        op.setOriginPublicKey(this.ledgerDTO.getPublicKey());
        op.setDestinationPublicKey(destinationPublicKey);

        try {
            if (this.operationResource.sendOperation(handleEnvelope(op), this.ledgerDTO.getPublicKey()))
                System.out.println("OK.");
            else System.out.println("NOK.");
        } catch(IOException e) {
            System.out.println("Error while ciphering the Envelope!");
        }
    }

    public void operationReceive(Long operationID) {
        if(clientKeyStore == null)
            System.out.println("Register First!");

        ReceiveOperationDTO rec = new ReceiveOperationDTO();
        rec.setOperationId(operationID);
        rec.setPublicKey(this.ledgerDTO.getPublicKey());

        try {
            if (this.operationResource.receiveOperation(handleEnvelope(rec), this.ledgerDTO.getPublicKey()))
                System.out.println("OK.");
            else System.out.println("NOK.");
        } catch(IOException e) {
            System.out.println("Error while ciphering the Envelope!");
        }
    }

    private Envelope handleEnvelope(CryptohdsDTO cryptohdsDTO) throws IOException {
        Message message = new Message(cryptohdsDTO, this.clientKeyStore);
        Envelope envelope = new Envelope();
        envelope.cipherEnvelope(message, cryptoServerPublicKey);

        return envelope;
    }
}
