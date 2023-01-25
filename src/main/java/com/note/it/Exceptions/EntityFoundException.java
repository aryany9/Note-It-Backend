package com.note.it.Exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class EntityFoundException extends NoteItException{
    public EntityFoundException(final HttpStatus status, final String msg) {
        super(status, msg);
    }

}
