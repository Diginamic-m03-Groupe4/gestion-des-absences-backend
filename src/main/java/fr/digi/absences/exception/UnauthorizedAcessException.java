package fr.digi.absences.exception;

public class UnauthorizedAcessException extends RuntimeException{

    public UnauthorizedAcessException(String message){
        super(message);
    }
}
