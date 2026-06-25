package br.gov.sp.fatec.lucas.marketplace.controllers.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
