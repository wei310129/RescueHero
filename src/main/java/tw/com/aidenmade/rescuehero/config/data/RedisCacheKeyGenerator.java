package tw.com.aidenmade.rescuehero.config.data;

import lombok.NonNull;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component("customKeyGenerator")
public class RedisCacheKeyGenerator implements KeyGenerator {
    @Override
    public @NonNull Object generate(@NonNull Object target, @NonNull Method method, @NonNull Object... params) {
        String className = target.getClass().getName();
        String methodName = method.getName();
        if (params.length == 0) {
            return String.format("%s.%s", className, methodName);
        }
        String paramString = Arrays.stream(params)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        return String.format("%s.%s(%s)", className, methodName, paramString);
    }
}
