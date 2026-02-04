package Exceptions;

public class NotAFileException extends RuntimeException{
    public NotAFileException(String message){
        super(message);
    }
}
