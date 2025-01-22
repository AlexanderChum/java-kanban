package main.exceptions;

public class TypeMismatchException extends RuntimeException {
    public TypeMismatchException(String message) {
        super(message);
    }
}
