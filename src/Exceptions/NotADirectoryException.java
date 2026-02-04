package Exceptions;

public class NotADirectoryException extends RuntimeException{
    public NotADirectoryException(String message){
        super(message);
    }
}
