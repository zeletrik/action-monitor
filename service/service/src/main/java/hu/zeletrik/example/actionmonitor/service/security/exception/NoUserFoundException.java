package hu.zeletrik.example.actionmonitor.service.security.exception;

public class NoUserFoundException extends RuntimeException {

    public NoUserFoundException(String msg) {
        super(msg);
    }
}
