package com.ekozah.chatmock.utils;

import java.util.HashSet;
import java.util.Set;

public class StringUtils {
    final String lexicon = "abcdefghijklmnopqrstuvwxyz";
    final String lexiconLong  = "abc de fghi jkl mnopqr s tuvw xyz ";
    final java.util.Random rand = new java.util.Random();
    final Set<String> stringSet = new HashSet<String>();

    /**
     * this function returns a unique string (maily used for first and last names)
     * @return unique string
     */
    public String getUniqueString() {
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(5)+5;
            for(int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if(stringSet.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        String word = builder.toString();
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    /**
     * this function returns a unique long string  that has a random length (with spaces)
     * @return string with spaces
     */
    public String getUniqueStringLongVersion() {
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(25)+2;
            for(int i = 0; i < length; i++) {
                builder.append(lexiconLong.charAt(rand.nextInt(lexiconLong.length())));
            }
            if(stringSet.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
    }
}
