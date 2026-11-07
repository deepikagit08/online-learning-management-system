package com.lms.exception;

/** Custom unchecked exception used to wrap SQL / business errors. */
public class LmsException extends RuntimeException {
    public LmsException(String message) { super(message); }
    public LmsException(String message, Throwable cause) { super(message, cause); }
}
