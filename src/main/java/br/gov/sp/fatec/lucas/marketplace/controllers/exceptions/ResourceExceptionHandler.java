package br.gov.sp.fatec.lucas.marketplace.controllers.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> database(DataIntegrityViolationException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                "Database Error",
                "This record already exists or violates integrity rules",
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;

        List<FieldMessage> list = e.getBindingResult().getFieldErrors()
                .stream()
                .map(f -> new FieldMessage(f.getField(), f.getDefaultMessage()))
                .toList();

        ValidationError err = new ValidationError(
                Instant.now(),
                status.value(),
                "Validation Error",
                "One or more fields are invalid",
                request.getRequestURI(),
                list
        );

        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> genericError(Exception e, HttpServletRequest request){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StandardError err = new StandardError(
                Instant.now(),
                status.value(),
                "Internal Server Error",
                "An unexpected error occurred on the server",
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(err);
    }
}
