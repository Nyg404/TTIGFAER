# 🚀 TTIGFAER — Spring-фреймворк для создания Telegram-ботов

# Прошу прощения. Но проект временно закрыт. Смотрите наш Телеграмм-Канал: [https://t.me/TTIGFAER](https://t.me/TTIGFAER)

Добро пожаловать в **TTIGFAER** — лёгкий и удобный Spring-фреймворк для быстрого и простого создания Telegram-ботов!

> В этом кратком руководстве вы быстро научитесь подключать и использовать фреймворк.

---

## 📚 Подробная документация

> Подробнее на GitBook:  
> [https://fraemworktelegram.gitbook.io/ttigfaer](https://fraemworktelegram.gitbook.io/ttigfaer)

---

## 1️⃣ Подключение зависимости

![Maven Central](https://img.shields.io/maven-central/v/io.github.nyg404/ttigfaer.svg)

Добавьте в ваш `pom.xml`:

```xml
<dependency>
  <groupId>io.github.nyg404</groupId>
  <artifactId>ttigfaer</artifactId>
  <version>1.7-SNAPSHOT</version> <!-- Замените на актуальную версию -->
</dependency>
```

---

## 2️⃣ Создание класса бота

Создайте класс, наследующий `CustomBot`, и зарегистрируйте его как Spring-компонент:

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

## 3️⃣ Создание команд с аннотациями

TTIGFAER использует **аннотации** для регистрации обработчиков событий и команд — быстро и удобно!

| Аннотация         | Описание                                                                                   |
|-------------------|--------------------------------------------------------------------------------------------|
| `@Handler`        | Помечает метод как обработчик события (команда, сообщение, callback и др.)                 |
| `@TAsync`         | Запускает метод асинхронно — не блокирует основной поток                                 |
| `@TimeBot`        | Ограничивает частоту вызова метода (лимиты, окна, задержки)                              |

---

### 🔥 Пример команды с аннотациями

```java
import io.github.nyg404.ttigfaer.api.Annotations.Handler;
import io.github.nyg404.ttigfaer.api.Annotations.TAsync;
import io.github.nyg404.ttigfaer.api.Annotations.TimeBot;
import io.github.nyg404.ttigfaer.core.Enum.HandlerType;

public class TestCommand extends CommandHelper {

    @Handler(HandlerType.ON_MESSAGE)                     // Обработка всех сообщений
    @Handler(HandlerType.ON_CALLBACK_QUERY)              // Обработка callback-запросов
    @Handler(value = HandlerType.REGISTER_COMMAND, commands = {"абоба", "абоба1"}) // Регистрация команд
    @TimeBot(limit = 1, limitWindows = 1, delay = 5)     // Лимит: 1 вызов в 1 сек., задержка 5 сек.
    @TAsync                                              // Асинхронное выполнение
    public void yourCommandMethod() {
        // Логика команды
    }
}
```

---

## 4️⃣ Описание аннотаций

### 📌 `@Handler`

Позволяет обрабатывать разные типы событий:

| Тип события                      | Описание                                              |
|---------------------------------|-------------------------------------------------------|
| `REGISTER_COMMAND`               | Регистрация команд (например, `/start`)                |
| `ON_MESSAGE`                    | Обработка любых сообщений                              | |
| `ON_CALLBACK_QUERY`             | Обработка callback-запросов от Inline-кнопок          |
| `RESPOND_TO_BOT_MESSAGE`        | Обработка ответов на сообщения бота                    |

**Параметры:**

- `value` — тип события (`HandlerType`)
- `commands` — список команд (для `REGISTER_COMMAND`)
- `callBack` — строка для обработки конкретного callback-запроса
- `filters` — можно указывать фильтры (для `ON_MESSAGE`)

**Пример:**

```java
@Handler(value = HandlerType.REGISTER_COMMAND, commands = {"start"})
public void onStart(MessageContext ctx) {
    ctx.sendMessage("Привет! Я бот.");
}
```

---

### ⚡ `@TAsync`

Асинхронное выполнение метода — полезно для долгих операций, чтобы не блокировать основной поток.

```java
@TAsync
@Handler(HandlerType.ON_MESSAGE)
public void onAnyMessage(MessageContext ctx) {
    // Асинхронная обработка
}
```

---

### ⏳ `@TimeBot`

Ограничивает частоту вызовов метода (чтобы избежать спама):

| Параметр     | Описание                         |
|--------------|----------------------------------|
| `limit`      | Максимум вызовов в окне          |
| `limitWindows` | Длительность окна в секундах    |
| `delay`      | Задержка перед выполнением (сек) |

Пример: 1 вызов в 1-секундном окне, с задержкой 5 секунд:

```java
@TimeBot(limit = 1, limitWindows = 1, delay = 5)
public void limitedCommand(MessageContext ctx) {
    ctx.sendMessage("Команда с ограничением частоты.");
}
```

---

## 5️⃣ Конфигурация `application.yml`

```yaml
bot:
  token: "ВАШ_ТОКЕН_БОТА"
  prefix: "/"  # Префикс команд (например, "/")

ttigfaer:
  async:
    thread-name-prefix: префикс  # Префикс для имён потоков
    core-pool-size: 5            # Минимум потоков
    max-pool-size: 10            # Максимум потоков
    queue-capacity: 50           # Вместимость очереди задач
```

---

## 💡 Советы и рекомендации

- Используйте `@TAsync` для тяжёлых операций — чтобы не тормозить бота.
- Применяйте `@TimeBot` для защиты от спама и ограничения вызовов.
- Разделяйте логику по типам событий с помощью разных `HandlerType`.
- Объединяйте регистрацию команд в одном методе с `@Handler(value = HandlerType.REGISTER_COMMAND, commands = {...})`.
- Следите за новостями и обновлениями в нашем телеграм-канале: [https://t.me/TTIGFAER](https://t.me/TTIGFAER)

---

Спасибо, что выбрали **TTIGFAER!**  
Если есть вопросы или предложения — всегда рады помочь! 🙌