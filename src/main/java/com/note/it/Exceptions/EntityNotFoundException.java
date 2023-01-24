package com.note.it.Exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class EntityNotFoundException extends NoteItException {
    public EntityNotFoundException(final HttpStatus status, final String msg) {
        super(status, msg);
    }
}
