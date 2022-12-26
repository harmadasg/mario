package util;

public class Time {
    private static final long TIME_AT_START = System.nanoTime();

    public static float getTime() {
        return (float)((System.nanoTime() - TIME_AT_START) * 1E-9);
    }
}
