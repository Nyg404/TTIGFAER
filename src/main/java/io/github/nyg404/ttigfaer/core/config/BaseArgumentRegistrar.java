package io.github.nyg404.ttigfaer.core.config;

import io.github.nyg404.ttigfaer.core.Utils.ArgumentRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BaseArgumentRegistrar {

    private final ArgumentRegistry argumentRegistry;

    public BaseArgumentRegistrar(ArgumentRegistry argumentRegistry) {
        this.argumentRegistry = argumentRegistry;
    }

    @PostConstruct
    public void init() {
        registerBaseArguments();
    }

    private void registerBaseArguments() {

    }

}

