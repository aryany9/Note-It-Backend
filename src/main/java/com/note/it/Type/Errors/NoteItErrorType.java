package com.note.it.Type.Errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum NoteItErrorType {
    SIGNATURE_DOES_NOT_MATCH(HttpStatus.BAD_REQUEST, "SignatureDoesNotMatch"),

    ENITY_NOT_FOUND(HttpStatus.BAD_REQUEST, "Entity Not Found"),

    ENTITY_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "Entity Already Exist"),

    IDEMPOTENCYERROR(HttpStatus.CONFLICT, "IdempotencyError");

    private HttpStatus httpStatus;

    private String errorCode;
}
