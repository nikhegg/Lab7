package exceptions;

public class RecursionException extends RuntimeException {
    /**
     *
     */
    public RecursionException() {}

    /**
     * @param msg
     */
    public RecursionException(String msg) {
        super(msg);
    }
}
