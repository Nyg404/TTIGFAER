package io.github.nyg404.ttigfaer.core.Utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ArgumentRegistry {
    private final Map<Class<?>, Object> arguments = new HashMap<>();

    public void register(Object instance) {
        arguments.put(instance.getClass(), instance);
    }

    public <T> void register(Class<T> clazz, T instance) {
        arguments.put(clazz, instance);
    }

    public <T> T get(Class<T> clazz) {
        return clazz.cast(arguments.get(clazz));
    }

    public Object getRaw(Class<?> clazz) {
        return arguments.get(clazz);
    }


}
