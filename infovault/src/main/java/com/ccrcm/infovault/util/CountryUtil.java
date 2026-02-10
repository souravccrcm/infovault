package com.ccrcm.infovault.util;

import java.util.Locale;

public final class CountryUtil {
    private CountryUtil() {}

    public static String toIso2(String countryName) {
        if (countryName == null || countryName.isBlank()) {
            return null;
        }

        for (String iso : Locale.getISOCountries()) {
            Locale locale = new Locale("", iso);
            if (locale.getDisplayCountry(Locale.ENGLISH)
                    .equalsIgnoreCase(countryName)) {
                return iso;
            }
        }
        return countryName; // fallback (don’t break response)
    }
}
