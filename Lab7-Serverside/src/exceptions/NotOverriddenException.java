package exceptions;

public class NotOverriddenException extends RuntimeException {
    /**
     * @param msg
     */
    public NotOverriddenException(String msg) {
        super(msg);
    }
}
