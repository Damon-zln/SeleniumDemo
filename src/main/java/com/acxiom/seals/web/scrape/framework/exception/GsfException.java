package com.acxiom.seals.web.scrape.framework.exception;

public class GsfException extends Exception {

    private static final long serialVersionUID = -5383256011057994313L;

    private final String code;

    private final String message;

    public GsfException(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }


    public String getMessage() {
        return message;
    }
}
