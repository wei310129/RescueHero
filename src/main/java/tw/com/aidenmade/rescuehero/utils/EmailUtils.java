package tw.com.aidenmade.rescuehero.utils;

public class EmailUtils {

    public static boolean isValidEmailStrict(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.matches(regex);
    }
}
