package tw.com.aidenmade.rescuehero.utils;

public class ExceptionUtils {

    public ExceptionUtils() {
        throw new AssertionError("No ExceptionUtils instances for you!");
    }

    public static Exception cannotFindId(Long id) {
        return new IllegalArgumentException("找不到這個id: %d".formatted(id));
    }

}
