package Exceptions;

public class QueryTypeUnsupported  extends RuntimeException {
    public QueryTypeUnsupported( String msg ){
        super(msg);
    }
}
