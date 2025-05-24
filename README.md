# TTIGFAER — Spring-фреймворк для создания Telegram-ботов



Добро пожаловать в TTIGFAER — простой и удобный Spring-фреймворк для быстрого создания Telegram-ботов.

В этом руководстве вы быстро узнаете, как подключить и использовать фреймворк.

---

## 1. Подключение зависимости

Добавьте в ваш `pom.xml` следующую зависимость:

```xml
<dependency>
  <groupId>io.github.nyg404</groupId>
  <artifactId>ttigfaer</artifactId>
  <version>1.6.2-SNAPSHOT</version> <!-- замените на актуальную версию -->
</dependency>
```



---

## 2. Создание класса бота

Создайте класс, наследующий `CustomBot` и добавьте аннотацию `@Component` для регистрации в Spring:

```java
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class Ваш класс extends CustomBot {
    public PriorBot(BotSettings botSettings, @Lazy CommandManager commandManager) {
        super(botSettings, commandManager);
    }
}
```

---

## 3. Создание команд с аннотациями

Пример класса с командами и обработчиками:

```java
import io.github.nyg404.ttigfaer.api.Annotations.Handler;
import io.github.nyg404.ttigfaer.api.Annotations.TAsync;
import io.github.nyg404.ttigfaer.api.Annotations.TimeBot;
import io.github.nyg404.ttigfaer.core.Commands.CommandHelper;
import io.github.nyg404.ttigfaer.core.Enum.HandlerType;

public class TestCommand extends CommandHelper {

    @Handler(HandlerType.ON_MESSAGE) // Обработка сообщений от пользователя
    @Handler(HandlerType.ON_CALLBACK_QUERY) // Обработка callback-запросов
    @Handler(value = HandlerType.REGISTER_COMMAND, commands = {"абоба", "абоба1"}) // Регистрация команд
    @TimeBot(limit = 1, limitWindows = 1, delay = 5) // Ограничение частоты вызова
    @TAsync // Асинхронное выполнение
    public void yourCommandMethod() {
        // Логика команды
    }
}
```

---

## 4. Конфигурация `application.yml`

```yaml
bot:
  token: "ВАШ_ТОКЕН_БОТА"
  prefix: "/" # Префикс для команд (например, "/")

ttigfaer:
  async:
    thread-name-prefix: префикс #Можете указывать свой префикс
    core-pool-size: 5        # Минимальное количество потоков
    max-pool-size: 10        # Максимальное количество потоков
    queue-capacity: 50       # Вместимость очереди задач
```

---

## Дополнительно

- Следите за актуальной версией через телеграмм канал - https://t.me/TTIGFAER.
- Вопросы и предложения — всегда приветствуются!

Спасибо за использование TTIGFAER!