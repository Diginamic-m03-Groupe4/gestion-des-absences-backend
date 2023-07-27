package fr.digi.absences.exception;

public class DuplicateIdentifierException extends RuntimeException {
    public DuplicateIdentifierException(String message){
        super(message);
    }
}
