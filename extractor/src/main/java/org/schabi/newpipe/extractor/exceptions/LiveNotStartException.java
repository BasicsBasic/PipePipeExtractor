package org.schabi.newpipe.extractor.exceptions;

public class LiveNotStartException extends ParsingException{
    public LiveNotStartException(String message) {
        super(message);
    }

    public LiveNotStartException(String message, Throwable cause) {
        super(message, cause);
    }
}
