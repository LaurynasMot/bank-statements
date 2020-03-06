package com.banking.bankstatements.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class BaseException extends Throwable {
    public BaseException(String base_exception) {
        super(base_exception);
    }
}
