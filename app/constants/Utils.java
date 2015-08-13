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
}
