package com.sec.cryptohdsclient.web.rest.errors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sec.cryptohdsclient.web.rest.exceptions.CryptohdsRestException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class CustomRestExceptionHandler implements ResponseErrorHandler {

    private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

    public boolean hasError(ClientHttpResponse response) throws IOException {
        return errorHandler.hasError(response);
    }

    public void handleError(ClientHttpResponse response) throws IOException, CryptohdsRestException {
        ObjectMapper mapper = new ObjectMapper();

        ApiError result = mapper.readValue(response.getBody(), ApiError.class);
        CryptohdsRestException exception = new CryptohdsRestException(result.getMessage());

        throw exception;
    }
}
