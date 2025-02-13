package Exceptions;

public class ColoumNotExist extends RuntimeException {
    
    public ColoumNotExist( String msg ){
        super(msg);
    }
}
