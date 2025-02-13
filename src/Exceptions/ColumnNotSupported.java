package Exceptions;

public class ColumnNotSupported extends RuntimeException {
    public ColumnNotSupported( String msg) {
         super(msg);
    }
}
