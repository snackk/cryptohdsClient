package com.sec.cryptohdsclient.web.rest.exceptions;

public class CryptohdsRestException extends RuntimeException {

	private static final long serialVersionUID = -541274631102609914L;

	private String message;

    public CryptohdsRestException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
