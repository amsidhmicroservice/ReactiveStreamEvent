package com.amsidh.reactive.reactivestreamevent.exception;

import lombok.Data;

@Data
public class ServiceException extends RuntimeException {
    private Integer statusCode;

    public ServiceException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
