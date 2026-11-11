/*
 * LookupNames.java
 * Author: Inga Plati
 * 230126634
 */
package za.ac.cput.util;

import java.util.Locale;

public final class LookupNames {

    private LookupNames() {
    }

    public static String clean(String text) {
        return text == null ? "" : text.trim().replaceAll("\\s+", " ");
    }

    public static String key(String text) {
        return text == null ? "" : text.toLowerCase(Locale.ROOT).replaceAll("[^\\p{L}\\p{N}]", "");
    }
}
