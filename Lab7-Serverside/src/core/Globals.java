package core;
import exceptions.NonNumberStringException;

public class Globals {
    private static double version = 2.4;
    private static String envPath = System.getenv("VECTOR_COLLECTION");
    private static long routesCreated = 0;

    /**
     * @return
     */
    public static double getVersion() {
        return version;
    }

    /**
     * @return
     */
    public static String getEnvPath() {
        return envPath;
    }

    /**
     * @return
     */
    public static long getRoutesCreated() {
        return routesCreated;
    }

    /**
     *
     */
    public static void incRoutesCreated() {
        routesCreated++;
    }

    /**
     *
     */
    public static void decRoutesCreated() {
        routesCreated--;
    }

    /**
     * @param number
     */
    public static void modifyRoutesCreated(long number) {
        routesCreated = number;
    }

    /**
     * @param word
     * @return
     */
    public static boolean isInt(String word) {
        try {
            Integer.parseInt(word);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    /**
     * @param word
     * @return
     * @throws NonNumberStringException
     */
    public static int getInt(String word) throws NonNumberStringException {
        if(Globals.isInt(word)) return Integer.parseInt(word);
        else throw new NonNumberStringException("Given argument is not a number");
    }

    /**
     * @param word
     * @return
     */
    public static boolean isDouble(String word) {
        try {
            Double.parseDouble(word);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    /**
     * @param word
     * @return
     * @throws NonNumberStringException
     */
    public static double getDouble(String word) throws NonNumberStringException {
        if(Globals.isDouble(word)) return Double.parseDouble(word);
        else throw new NonNumberStringException("Given argument is not a number");
    }
}
