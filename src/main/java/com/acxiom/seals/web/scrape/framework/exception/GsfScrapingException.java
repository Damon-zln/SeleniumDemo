package com.acxiom.seals.web.scrape.framework.exception;

public class GsfScrapingException extends GsfException {

    public GsfScrapingException(String message){
        super("", message);
    }

    public GsfScrapingException(String code, String message) {
        super(code, message);
    }

    private static final long serialVersionUID = -7416640750240144286L;
}
