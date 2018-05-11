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
import java.util.HashMap;
import java.util.Map;

import com.sec.cryptohdslibrary.service.dto.OperationDTO;
import com.sec.cryptohdslibrary.service.dto.ReceiveOperationDTO;
import org.springframework.stereotype.Service;

@Service
public class ClientHandler {

    /*Spring Resources to Autowire*/
    private final LedgerResource ledgerResource;

    private final OperationResource operationResource;

    private final SecurityResource securityResource;

    /*Client Handler*/
    private KeyStoreImpl clientKeyStore;

    private LedgerDTO ledgerDTO;

    /*Sequence numbers for each server*/
    private Map<String, Integer> sequenceNumbers = new HashMap<>();

    /*Store servers*/
    private HashMap<String,String> cryptoServersPubKeys;

    public ClientHandler(LedgerResource ledgerResource, SecurityResource securityResource, OperationResource operationResource) {
        this.ledgerResource = ledgerResource;
        this.securityResource = securityResource;
        this.operationResource = operationResource;

        /*Initialize public keys for each server*/
        this.cryptoServersPubKeys = this.securityResource.getPublicKey();

        initServersSeqNumber(-1);
    }

    private KeyStoreImpl getKeyStore() {
        return clientKeyStore;
    }

    private void initServersSeqNumber(int seqNumber) {
        /*Initialize sequence number for each server*/
        for(String ip : this.cryptoServersPubKeys.keySet()) {
            sequenceNumbers.put(ip, seqNumber);
        }
    }

    public void ledgerRegister(String ledgerName, String ledgerPassword) {

        /*Initialize KeyStore*/
        this.clientKeyStore = new KeyStoreImpl(ledgerName, ledgerPassword);

        /*Create LedgerDTO with his public key and encode as base64. Also store LedgerDTO*/
        this.ledgerDTO = new LedgerDTO(ledgerName, CipherInstance.encodePublicKey(clientKeyStore.getkeyPairHDS().getPublic()));

        try {
            if (this.ledgerResource.createLedger(handleEnvelope(this.ledgerDTO), this.ledgerDTO.getPublicKey())){
                System.out.println("OK.");
                initServersSeqNumber(0);
            }
            else {
                System.out.println("NOK.");
                this.clientKeyStore = null;
            }

        } catch (CryptohdsRestException e) {
            if(e.getMessage().contains("already")) {

                try {
                    this.ledgerResource.updateLedgerSeqNumber(handleEnvelope(this.ledgerDTO), this.ledgerDTO.getPublicKey(), clientKeyStore, this.sequenceNumbers);
                    System.out.println("Logged in with Ledger: " + ledgerName);

                } catch(CryptohdsRestException ex) {
                    System.out.println(ex.getMessage());
                    this.clientKeyStore = null;
                }
            } else {
                System.out.println(e.getMessage());
                this.clientKeyStore = null;
            }
        }
    }

    public void ledgerCheckBalance() {
        if(clientKeyStore == null)
            System.out.println("Register First!");

        try {
            if (this.ledgerResource.checkBalance(handleEnvelope(this.ledgerDTO), this.ledgerDTO.getPublicKey(), clientKeyStore, this.sequenceNumbers)) {
                System.out.println("OK.");
            }
            else System.out.println("NOK.");

        } catch(CryptohdsRestException e) {
            System.out.println(e.getMessage());
        }
    }

    public void ledgerAudit() {
        if(clientKeyStore == null)
            System.out.println("Register First!");

        try {
            if (this.ledgerResource.audit(handleEnvelope(this.ledgerDTO), this.ledgerDTO.getPublicKey(), clientKeyStore, this.sequenceNumbers)) {
                System.out.println("OK.");
            }
            else System.out.println("NOK.");

        } catch(CryptohdsRestException e) {
            System.out.println(e.getMessage());
        }
    }

    public void operationSend(Long value, String destinationPublicKey) {
        if(clientKeyStore == null)
            System.out.println("Register First!");

        OperationDTO op = new OperationDTO(ledgerDTO.getPublicKey(), destinationPublicKey, value);

        try {
            if (this.operationResource.sendOperation(handleEnvelope(op), this.ledgerDTO.getPublicKey())) {
                System.out.println("OK.");
            }
            else System.out.println("NOK.");

        } catch(CryptohdsRestException e) {
            System.out.println(e.getMessage());
        }
    }

    public void operationReceive(Long operationID) {
        if(clientKeyStore == null)
            System.out.println("Register First!");

        ReceiveOperationDTO rec = new ReceiveOperationDTO();
        rec.setOperationId(operationID);
        rec.setPublicKey(this.ledgerDTO.getPublicKey());

        try {
            if (this.operationResource.receiveOperation(handleEnvelope(rec), this.ledgerDTO.getPublicKey())) {
                System.out.println("OK.");
            }
            else System.out.println("NOK.");

        } catch(CryptohdsRestException e) {
            System.out.println(e.getMessage());
        }
    }

    /*Used to Cypher an Envelope to Send*/
    private HashMap<String, Envelope> handleEnvelope(CryptohdsDTO cryptohdsDTO) throws CryptohdsRestException {
        Message message = new Message(cryptohdsDTO, this.clientKeyStore, -1);
        
        HashMap<String, Envelope> ips2return = new HashMap<>();
        for(String ip : cryptoServersPubKeys.keySet()) {

            message.setSeqNumber(this.sequenceNumbers.get(ip));

            Envelope envelope = new Envelope();
            try {
            	envelope.cipherEnvelope(message, cryptoServersPubKeys.get(ip));
                ips2return.put(ip, envelope);

            } catch(IOException e) {
                throw new CryptohdsRestException("Error while ciphering the Envelope!");
            }        	
        }

        return ips2return;
    }
}
