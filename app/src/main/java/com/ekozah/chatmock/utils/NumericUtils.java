package com.ekozah.chatmock.utils;

public class NumericUtils {

    private static final long LIMIT = 10000000000L;
    private static long last = 0;

    /**
     * this function returns a unique long integer as an id
     * @return id
     */
    public static long getID() {
        // 10 digits.
        long id = System.currentTimeMillis() % LIMIT;
        if ( id <= last ) {
            id = (last + 1) % LIMIT;
        }
        return last = id;
    }
}
