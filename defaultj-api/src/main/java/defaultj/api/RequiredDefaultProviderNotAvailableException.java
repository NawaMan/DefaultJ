package defaultj.api;

public class RequiredDefaultProviderNotAvailableException extends RuntimeException {
    
    private static final long serialVersionUID = -8886300301087529369L;
    
    RequiredDefaultProviderNotAvailableException(Exception cause) {
        super(cause);
    }
    
}
