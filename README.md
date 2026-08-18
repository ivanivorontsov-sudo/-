# Змейка (Snake Game) для Android

Классическая игра «Змейка» на Kotlin.

**Репозиторий:** https://github.com/ivanivorontsov-sudo/-

## Как собрать APK

### Способ 1: Android Studio (самый простой)
1. Скачайте проект (Code → Download ZIP) или клонируйте:
   ```
   git clone https://github.com/ivanivorontsov-sudo/-.git
   ```
2. Откройте папку проекта в **Android Studio**
3. Дождитесь синхронизации Gradle (может потребоваться интернет)
4. Меню **Build → Build Bundle(s) / APK(s) → Build APK(s)**
5. Готовый APK будет здесь:
   `app/build/outputs/apk/debug/app-debug.apk`

Установите APK на телефон (включите «Установку из неизвестных источников»).

### Способ 2: Через командную строку (если установлен Android SDK)
```bash
./gradlew assembleDebug
```

## Управление в игре
- Касайтесь экрана в нужном направлении (относительно головы змейки)
- После проигрыша нажмите на экран, чтобы начать заново

## Что внутри
- `SnakeGameView.kt` — вся логика игры
- Простая и понятная реализация без лишних библиотек

Приятной игры! 🐍
