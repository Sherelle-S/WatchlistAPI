package com.cbfacademy.apiassessment.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.SERVICE_UNAVAILABLE)
    public class ExternalAPIResponseUnavailable extends IllegalArgumentException {
    public ExternalAPIResponseUnavailable(){}
    public ExternalAPIResponseUnavailable(String message){
        super(message);
    }

    public ExternalAPIResponseUnavailable(Throwable cause){
    super(cause);
    }

    public ExternalAPIResponseUnavailable(String message, Throwable cause){
        super(message, cause);
    }
}
