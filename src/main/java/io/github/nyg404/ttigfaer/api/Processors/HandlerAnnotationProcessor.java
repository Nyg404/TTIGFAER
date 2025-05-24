package io.github.nyg404.ttigfaer.api.Processors;

import io.github.nyg404.ttigfaer.api.Annotations.Handler;
import io.github.nyg404.ttigfaer.core.Enum.HandlerType;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;
@SupportedAnnotationTypes("io.github.nyg404.ttigfaer.api.Annotations.Handler")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class HandlerAnnotationProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Handler.class)) {
            if (element.getKind() != ElementKind.METHOD) {
                error(element, "Аннотация @Handler может быть применена только к методам.");
                continue;
            }

            Handler handler = element.getAnnotation(Handler.class);
            HandlerType handlerType = handler.value();
            String[] commands = handler.commands();
            String callBack = handler.callBack();

            // Проверка для commands
            if (!handlerType.equals(HandlerType.REGISTER_COMMAND) && commands.length > 0) {
                error(element, "Атрибут 'commands' допустим только для HandlerType.REGISTER_COMMAND.");
            }

            // Проверка для callBack
            if (!handlerType.equals(HandlerType.ON_CALLBACK_QUERY) && !callBack.isEmpty()) {
                error(element, "Атрибут 'callBack' допустим только для HandlerType.CALLBACK.");
            }
        }
        return true;
    }

    private void error(Element element, String message) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }
}
