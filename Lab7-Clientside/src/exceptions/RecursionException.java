package exceptions;

public class RecursionException extends Exception {
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
