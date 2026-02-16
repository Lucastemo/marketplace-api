package br.gov.sp.fatec.lucas.marketplace.controllers.exceptions;

public record FieldMessage(
        String fieldName,
        String message
) {}
