package util;

/**
 * Created by MarioJ on 12/05/15.
 */
public class Utils {

    public static String joinArray(String[] strings, String separator) {

        StringBuilder stringBuilder = new StringBuilder();

        for (String str : strings) {
            stringBuilder.append("'" + str + "'" + separator);
        }

        return stringBuilder.substring(0, stringBuilder.length() - 1).toString();
    }

}
