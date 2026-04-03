package com.ladi.stour.common;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class SlugGenerator {
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-\\s]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s_]");

    public static String generateSlug(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        String nfd = Normalizer.normalize(input, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(nfd).replaceAll("");
        slug = WHITESPACE.matcher(slug).replaceAll("-");
        slug = slug.toLowerCase();
        // Remove consecutive dashes
        slug = slug.replaceAll("-+", "-");
        // Remove leading and trailing dashes
        slug = slug.replaceAll("^-+|-+$", "");

        return slug;
    }
}
