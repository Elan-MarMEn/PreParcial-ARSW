package edu.eci.arsw.exams.moneylaunderingapi.service;

public class MoneyLaunderingServiceException extends Exception{
    public MoneyLaunderingServiceException(String message) {
        super(message);
    }

    public MoneyLaunderingServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
