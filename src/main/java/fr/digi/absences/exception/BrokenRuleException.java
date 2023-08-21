package fr.digi.absences.exception;


public class BrokenRuleException extends RuntimeException {
    public BrokenRuleException(String message){
        super(message);
    }
}
