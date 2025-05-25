
# TTIGFAER — Spring-фреймворк для создания Telegram-ботов

Добро пожаловать в TTIGFAER — простой и удобный Spring-фреймворк для быстрого создания Telegram-ботов.

В этом руководстве вы быстро узнаете, как подключить и использовать фреймворк.

---

## 1. Подключение зависимости
![Maven Central](https://img.shields.io/maven-central/v/io.github.nyg404/ttigfaer.svg)

Добавьте в ваш `pom.xml` следующую зависимость:

```xml
<dependency>
  <groupId>io.github.nyg404</groupId>
  <artifactId>ttigfaer</artifactId>
  <version>1.7-SNAPSHOT</version> <!-- замените на актуальную версию -->
</dependency>
```

---

## 2. Создание класса бота

Создайте класс, наследующий `CustomBot`, и добавьте аннотацию `@Component` для регистрации в Spring:

```java
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class PriorBot extends CustomBot {
    public PriorBot(BotSettings botSettings, @Lazy CommandManager commandManager) {
        super(botSettings, commandManager);
    }
}
```

---

## 3. Создание команд с помощью аннотаций

TTIGFAER использует аннотации для удобной регистрации обработчиков событий и команд.

### Основные аннотации:

| Аннотация         | Описание                                                                                          |
|-------------------|-------------------------------------------------------------------------------------------------|
| `@Handler`        | Обозначает метод как обработчик события. Можно указать тип события и список команд.             |
| `@TAsync`         | Выполняет метод асинхронно, не блокируя основной поток.                                         |
| `@TimeBot`        | Ограничивает частоту вызова метода (лимиты, окна, задержки).                                     |

---

### Пример команды с аннотациями

```java
import io.github.nyg404.ttigfaer.api.Annotations.Handler;
import io.github.nyg404.ttigfaer.api.Annotations.TAsync;
import io.github.nyg404.ttigfaer.api.Annotations.TimeBot;
import io.github.nyg404.ttigfaer.core.Enum.HandlerType;

public class TestCommand extends CommandHelper {

    @Handler(HandlerType.ON_MESSAGE) // Обработка любых сообщений
    @Handler(HandlerType.ON_CALLBACK_QUERY) // Обработка callback-запросов
    @Handler(value = HandlerType.REGISTER_COMMAND, commands = {"абоба", "абоба1"}) // Регистрация команд
    @TimeBot(limit = 1, limitWindows = 1, delay = 5) // Лимит: 1 вызов в 1 секунду, задержка 5 секунд
    @TAsync // Асинхронное выполнение
    public void yourCommandMethod() {
        // Ваша логика команды
    }
}
```

---

## 4. Описание аннотаций

### `@Handler`

Используется для обработки различных событий:

- `HandlerType.REGISTER_COMMAND` — регистрация команд (например, "/start").
- `HandlerType.ON_MESSAGE` — обработка любых сообщений.
- `HandlerType.ON_TEXT` — обработка текстовых сообщений.
- `HandlerType.ON_CALLBACK_QUERY` — обработка callback-запросов от Inline-кнопок.
- `HandlerType.RESPOND_TO_BOT_MESSAGE` — обработка ответов на сообщения бота.

**Параметры:**

- `value` — тип события из `HandlerType`.
- `commands` — массив команд (актуально для `REGISTER_COMMAND`).
- `callBack` — строка для обработки конкретного callback-запроса.

**Пример:**

```java
@Handler(value = HandlerType.REGISTER_COMMAND, commands = {"start"})
public void onStart(MessageContext ctx) {
    ctx.sendMessage("Привет! Я бот.");
}
```

---

### `@TAsync`

Позволяет выполнять метод асинхронно, что полезно для долгих операций, чтобы не блокировать основной поток.

```java
@TAsync
@Handler(HandlerType.ON_MESSAGE)
public void onAnyMessage(MessageContext ctx) {
    // Асинхронная обработка сообщения
}
```

---

### `@TimeBot`

Позволяет ограничить частоту вызовов метода:

- `limit` — максимальное количество вызовов в окне.
- `limitWindows` — длительность окна в секундах.
- `delay` — задержка перед выполнением метода (в секундах).

Пример ограничения: 1 вызов метода в 1-секундном окне с задержкой 5 секунд:

```java
@TimeBot(limit = 1, limitWindows = 1, delay = 5)
public void limitedCommand(MessageContext ctx) {
    ctx.sendMessage("Команда с ограничением частоты.");
}
```

---

## 5. Конфигурация `application.yml`

```yaml
bot:
  token: "ВАШ_ТОКЕН_БОТА"
  prefix: "/" # Префикс для команд (например, "/")

ttigfaer:
  async:
    thread-name-prefix: префикс # Префикс для потоков
    core-pool-size: 5           # Минимальное количество потоков
    max-pool-size: 10           # Максимальное количество потоков
    queue-capacity: 50          # Вместимость очереди задач
```

---

## 6. Советы и рекомендации

- Используйте аннотацию `@TAsync` для ресурсоёмких или долгих операций, чтобы бот не "зависал".
- Применяйте `@TimeBot` для защиты от спама и ограничения частоты вызовов.
- Используйте разные `HandlerType` для разделения логики по типам событий.
- Для удобства регистрации команд объединяйте их в одном методе с `@Handler(value = HandlerType.REGISTER_COMMAND, commands = {...})`.
- Следите за обновлениями в телеграм-канале: [https://t.me/TTIGFAER](https://t.me/TTIGFAER)

---

Спасибо, что выбрали TTIGFAER!  
Если есть вопросы или предложения — всегда рады помочь.