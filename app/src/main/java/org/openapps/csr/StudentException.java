package org.openapps.csr;


public class StudentException extends Exception    {

    public StudentException()  {
        super();
    }

    public StudentException(String message) {
        super(message);
    }

    public StudentException(String message,Throwable cause)  {
        super(message,cause);
    }
}