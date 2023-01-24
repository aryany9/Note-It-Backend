package com.note.it.Exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.note.it.Constants.DataFormatConstants;
import com.note.it.Type.Errors.NoteItErrorType;
import org.springframework.http.HttpStatus;

import java.util.Calendar;

public class NoteItException extends RuntimeException {
    private String version;

    private String txnid;

    @JsonFormat(pattern = DataFormatConstants.TIMESTAMP_OUTPUT_FORMAT)
    private Calendar timestamp;

    private String errorCode;

    private String errorMsg;

    private HttpStatus status;

    public NoteItException(
            final HttpStatus status,
            final String version,
            final String txnId,
            final String errorCode,
            final String errorMsg) {
        super();
        timestamp = Calendar.getInstance();
        this.status = status;
        this.version = version;
        this.txnid = txnId;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public NoteItException(final HttpStatus status, final String errorCode) {
        super();
        timestamp = Calendar.getInstance();
        this.status = status;
        this.errorCode = errorCode;
    }

    public NoteItException(final NoteItErrorType aaErrorType) {
        super();
        timestamp = Calendar.getInstance();
        this.status = aaErrorType.getHttpStatus();
        this.errorCode = aaErrorType.getErrorCode();
    }
}
