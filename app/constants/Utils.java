package constants;

/**
 * Created by gaylor on 08/13/2015.
 *
 */
public class Utils {

    public static String getMaxCharacters(String text, int max) {

        if (text == null || max < 0) {

            return "";
        }

        if (text.length() <= max) {

            return text;
        } else {

            return text.substring(0, max) + "...";
        }
    }

    /**
     * Convert a string into a integer if the string is correct
     * @param number The integer under String format
     * @return the number or -1
     */
    public static int parseInt(String number) {
        if (number != null && number.matches("^[\\-0-9]+$")) {
            return Integer.parseInt(number);
        } else {
            return -1;
        }
    }
}
