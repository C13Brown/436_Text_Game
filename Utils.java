package objectAdventure.common;
// $Id: Utils.java 326 2023-10-19 14:07:09Z aconover $

import objectAdventure.core.DescriptionType;
import objectAdventure.core.item.Item;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * General static utility methods.
 *
 * @author Adam J. Conover, COSC436/COSC716
 */
public final class Utils {
    /**
     * Private constructor to prevent instantiation.
     */
    private Utils() {
        // Prevent instantiation, this is a utility class.
    }

    /**
     * Capitalize the first letter of the string. If null is received, an empty
     * string is returned.
     *
     * @param str The string to capitalize
     * @return The capitalized string
     */
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        } else {
            if (str.length() == 1) {
                return str.toUpperCase();
            } else {
                return str.substring(0, 1).toUpperCase() + str.substring(1);
            }
        }
    }

    /**
     * Get a list of items in a list display format.
     *
     * @param itemCollection  The collection of items
     * @param descriptionType Either DescriptionType.SHORT or DescriptionType.LONG
     * @return The list of items in a string format
     */
    public static String getFormattedItemList(Collection<? extends Item> itemCollection,
                                              DescriptionType descriptionType) {
        return itemCollection
                .stream()
                .map(switch (descriptionType) {
                    case SHORT -> Item::getItemDisplayName;
                    case LONG -> Item::getItemFullDescription;
                })
                .map(Utils::capitalize)
                .map("*  "::concat)
                .collect(Collectors.joining("\n"));
    }

    /**
     * Concatenate a list of lists into a single list.
     *
     * @param lists The lists to concatenate
     * @param <T>   The type of the list
     * @return The concatenated list
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> List<T> concatLists(List<? extends T>... lists) {
        return Stream.of(lists)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    /**
     * Wrap input text to a specified width.
     * <p>
     * Note: Several styles of word wrapping algorithms exist, mainly "early wrapping or late wrapping". This is a late
     * wrapping algorithm, which means that the text does not wrap until the word is longer than the width. In early
     * wrapping algorithms, the text is wrapped as soon as the *next* word would exceed the specified width. "Late
     * wrapping" is simpler to implement, but doesn't work on displays with a fixed number of character columns.
     * (Algorithms, which attempt to keep the text as "fully justified" as possible exit, but those are a complete PITA.
     * I actually had to write code to do this in <a href="https://en.wikipedia.org/wiki/Zilog_Z80">Z80</a> assembly in
     * the late 1990s... fun times! (If you google for "TI-73 math mastery modules", you can probably find screenshots
     * from that project.)
     *
     * @param text   The text to wrap
     * @param width  The width of the wrapped text.
     * @param reflow If true, strip existing newlines and extra spaces.
     * @return The wrapped text.
     */
    public static String wrapText(String text, final int width, final boolean reflow) {
        // Get rid of new-lines and extra spaces.
        if (reflow) {
            text = text.replace("\n", " ").replaceAll("\\s+", " ");
        }

        // String builder to hold the wrapped text.
        StringBuilder sb = new StringBuilder();

        // Loop through the text, adding a new-line when the width is reached.
        // Yes, I'm pretending to be a C programmer here. :)
        for (int i = 0, col = 0; i < text.length(); i++, col++) {
            char theChar = text.charAt(i);

            // If we've reached the width (or we have a new line), add a new-line.
            if (col >= width && theChar == ' ' || theChar == '\n') {
                sb.append('\n');
                col = 0;
                continue;
            }

            // If we're at the beginning of a line, and we have a space, skip it.
            if (col == 0 && theChar == ' ') {
                continue;
            }

            // Add the character to the string builder.
            sb.append(theChar);
        }

        // Return the wrapped text.
        return sb.toString();
    }


    /**
     * Wrap text to a default width of 80 characters.
     *
     * @param text The text to wrap.
     * @return The wrapped text.
     * @see #wrapText(String, int, boolean)
     */
    public static String wrapText(String text) {
        return wrapText(text, 80, true);
    }
}
