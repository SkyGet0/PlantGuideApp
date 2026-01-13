# Plant Guide App

Справочник растений для Android, написанный на **Kotlin** с использованием **Jetpack Compose**. Приложение предоставляет подробную информацию о различных растениях, включая овощи, травы и комнатные растения, с поддержкой управления избранным и локальным хранилищем данных через Room.

---

## Возможности

- **Главный экран** – Навигационный центр с выбором категорий и быстрым доступом к избранному
- **Каталог растений** – Просмотр растений по категориям (Овощи, Травы, Комнатные растения)
- **Поиск** – Фильтрация растений по названию в реальном времени
- **Система избранного** – Добавление/удаление растений в избранное
- **Детальная информация о растениях** – Подробные описания, включая:
  - Краткие описания
  - Полные руководства по выращиванию с советами о температуре, требованиях к свету, питательных веществах и т.д.
  - Классификация по категориям
  - Изображения растений
- **Красивый интерфейс** – Современный дизайн Material 3 с интуитивной навигацией
- **Локальное хранилище данных** – Все данные сохраняются локально в базе данных Room

---

## Архитектура

Приложение следует архитектурному паттерну **MVVM (Model-View-ViewModel)** с четким разделением ответственности:

```
com.example.plantguideapp/
├── ui/                          # Слой представления (Jetpack Compose)
│   ├── home/
│   │   └── HomeScreen.kt       # Главный экран с выбором категорий
│   ├── navigation/
│   │   └── NavGraph.kt         # Настройка навигации с Compose Navigation
│   ├── plants/
│   │   ├── PlantListScreen.kt  # Отображение растений по категориям с поиском
│   │   ├── PlantDetailScreen.kt# Подробная информация о растении
│   │   └── FavoritePlantsScreen.kt # Экран избранных растений
│   └── theme/
│       ├── Color.kt             # Определение цветов
│       ├── Theme.kt             # Конфигурация Material 3 темы
│       └── Type.kt              # Стили типографики
├── data/                        # Слой данных (Room Database)
│   ├── Plant.kt                # Модель сущности растения
│   ├── PlantDao.kt             # Data Access Object с запросами Room
│   └── PlantDatabase.kt        # Настройка Room базы данных
├── repository/                  # Слой репозитория (Абстракция данных)
│   └── PlantRepository.kt      # Бизнес-логика и управление данными
├── viewmodel/                   # Слой ViewModel
│   └── PlantViewModel.kt       # Управление состоянием и логика UI
└── MainActivity.kt              # Точка входа приложения
```

### Слои архитектуры:

1. **Слой UI (Jetpack Compose)** – Composable функции для отображения интерфейса
2. **ViewModel** – Управляет состоянием, обрабатывает действия пользователя, предоставляет данные UI
3. **Repository** – Абстрагирует источники данных (база данных Room)
4. **Database (Room)** – Локальная SQLite база данных для хранения данных о растениях

---

## Стек технологий

| Технология | Назначение |
|-----------|---------|
| **Kotlin** | Основной язык программирования |
| **Jetpack Compose** | Современный декларативный фреймворк UI |
| **Material 3** | Компоненты Material Design |
| **Navigation Compose** | Навигация в приложении |
| **Room Database** | Локальная SQLite база данных |
| **Coroutines & Flow** | Асинхронные операции и реактивные потоки данных |
| **Lifecycle** | ViewModel и компоненты, осведомленные о жизненном цикле |
| **Android Studio** | IDE для разработки |

---

## Модель данных

### Сущность Plant

```kotlin
@Entity(tableName = "plants")
data class Plant(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,                  // Название растения (напр., "Базилик")
    val description: String,           // Краткое описание
    val longDescription: String,       // Подробное руководство с советами
    val category: String,              // Категория (Овощи, Травы, Комнатные)
    val imageResName: String,          // Имя ресурса drawable
    val isFavorite: Boolean = false    // Флаг избранного
)
```

### Схема базы данных:

Таблица **plants**:
- `id` (Integer, Primary Key, Auto-increment)
- `name` (Text)
- `description` (Text)
- `longDescription` (Text)
- `category` (Text)
- `imageResName` (Text)
- `isFavorite` (Boolean)

---

## Навигация по экранам

Приложение использует **Compose Navigation** с маршрутизацией через sealed class:

```
Главный экран
├── → Экран списка растений (категория)
│   └── → Экран деталей растения (id растения)
├── → Экран избранного
│   └── → Экран деталей растения (id растения)
└── Возврат назад
```

### Маршруты навигации:

- `home` – Главный экран
- `plant_list/{category}` – Растения по категориям
- `plant_detail/{id}` – Детали одного растения
- `favorites` – Список избранных растений

---

## Управление состоянием

### PlantViewModel

Управляет состоянием приложения с использованием **Kotlin Flow** и **StateFlow**:

```kotlin
val plants: StateFlow<List<Plant>>       // Отфильтрованные растения по категории и поиску
val favorites: StateFlow<List<Plant>>    // Избранные растения
val category: StateFlow<String>          // Текущая категория
val searchQuery: StateFlow<String>       // Запрос поиска

fun setCategory(cat: String)             // Обновить категорию
fun setSearchQuery(query: String)        // Обновить запрос поиска
fun toggleFavorite(plant: Plant)         // Добавить/удалить из избранного
fun getPlantById(id: Int): Flow<Plant>  // Получить одно растение
```

### Ключевые особенности:

- **Реактивные обновления** – UI автоматически обновляется при изменении данных
- **Комбинирование потоков** – Категория и запрос поиска объединены для фильтрации
- **Инициализация примеров данных** – Загружает начальные данные о растениях при первом запуске

---

## Зависимости

Ключевые библиотеки в `build.gradle.kts`:

```kotlin
// Jetpack Compose
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.foundation:foundation")

// Навигация
implementation("androidx.navigation:navigation-compose")

// Room Database
implementation("androidx.room:room-runtime")
implementation("androidx.room:room-ktx")
kapt("androidx.room:room-compiler")

// Lifecycle & ViewModel
implementation("androidx.lifecycle:lifecycle-viewmodel-compose")
implementation("androidx.lifecycle:lifecycle-runtime-ktx")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android")
```

---

## Начало работы

### Требования

- Android Studio версии Jellyfish или новее
- Kotlin 1.9.0+
- Android SDK 31 или выше
- Gradle 8.2+

### Установка

1. **Клонируйте репозиторий:**
   ```bash
   git clone https://github.com/SkyGet0/Plant-Guide-App.git
   cd Plant-Guide-App
   ```

2. **Откройте в Android Studio:**
   - File → Open → Выберите папку проекта
   - Ждите завершения синхронизации Gradle

3. **Соберите и запустите:**
   ```bash
   # Запуск на эмуляторе или подключенном устройстве
   ./gradlew installDebug
   ```

   Или используйте кнопку "Run" в Android Studio

4. **Первый запуск:**
   - Приложение автоматически загружает примеры данных о растениях
   - Перейдите на главный экран и изучайте категории

---

## Локальная база данных

### Настройка Room Database

Приложение использует **Room** для локальной SQLite базы данных:

```kotlin
@Database(entities = [Plant::class], version = 1, exportSchema = false)
abstract class PlantDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao
    
    companion object {
        fun getDatabase(context: Context): PlantDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                PlantDatabase::class.java,
                "plant_database"
            ).build()
        }
    }
}
```

### Операции с базой данных:

- **Чтение** – Запрос растений по категории, получение одного растения, выборка избранного
- **Создание** – Вставка примеров данных при первом запуске
- **Обновление** – Отметить растения как избранные/неизбранные
- **Удаление** – Очистка всех данных (функция администратора)

---

## UI и стилизация

### Цветовая схема

Приложение использует **зелену-основанную цветовую палитру**, оптимизированную для справочника растений:

```kotlin
private val LightGreen = Color(0xFFA8E6A3)  // #A8E6A3
private val MidGreen = Color(0xFF66CC66)    // #66CC66
private val DarkGreen = Color(0xFF339933)   // #339933
```

### Material 3 тема

- Поддержка динамических цветов (Android 12+)
- Поддержка темного/светлого режима
- Пользовательская типография с шкалами шрифтов Material 3

---

## 🔍 Ключевые детали реализации

### 1. Поиск в реальном времени с Kotlin Flow

```kotlin
val plants = combine(_category, _searchQuery) { category, query ->
    category to query
}.flatMapLatest { (category, query) ->
    repository.getPlantsByCategory(category).map { plants ->
        if (query.isBlank()) plants
        else plants.filter { it.name.contains(query, ignoreCase = true) }
    }
}.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
```

### 2. Управление избранным

```kotlin
fun toggleFavorite(plant: Plant) {
    viewModelScope.launch {
        repository.setFavorite(plant.id, !plant.isFavorite)
    }
}
```

### 3. Навигация с аргументами

```kotlin
navController.navigate(Screen.PlantDetail.createRoute(plantId))

// Безопасное создание маршрута:
object PlantDetail : Screen("plant_detail/{id}") {
    fun createRoute(id: Int) = "plant_detail/$id"
}
```

---

## Примеры данных

Приложение включает 4 примера растений, загруженных при первом запуске:

1. **Базилик** – Категория Травы
2. **Фикус** – Комнатные растения
3. **Огурцы** – Овощи
4. **Томаты** – Овощи

Каждое растение включает:
- Русское название
- Краткое описание
- Полное руководство по выращиванию (температура, pH, освещение, питательные вещества и т.д.)
- Классификацию по категориям
- Связанное изображение растения

---

## Возможные улучшения в будущем
- Напоминания о поливе растений/уведомления
- Советы по уходу и календарь
- Синхронизация избранного в облаке
- Поддержка нескольких языков
- Продвинутая фильтрация (по сложности, времени роста и т.д.)
- Личные заметки и журналы растений

---

## 📄 Итоговая структура проекта

```
PlantGuideApp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── kotlin/com/example/plantguideapp/
│   │   │   │   ├── ui/
│   │   │   │   ├── data/
│   │   │   │   ├── repository/
│   │   │   │   ├── viewmodel/
│   │   │   │   └── MainActivity.kt
│   │   │   ├── res/
│   │   │   │   ├── drawable/        # Изображения растений и иконки
│   │   │   │   ├── values/          # Строки, цвета, размеры
│   │   │   │   └── layout/          # Унаследованные XML-макеты (если есть)
│   │   │   └── AndroidManifest.xml
│   │   └── test/                    # Модульные тесты
│   └── build.gradle.kts             # Зависимости модуля
├── build.gradle.kts                 # Корневая конфигурация сборки
├── settings.gradle.kts              # Настройки проекта
└── gradle/                          # Gradle wrapper
```

---

## Безопасность и лучшие практики

- Нет жестко закодированных секретов или API-ключей
- Валидация входных данных для поисковых запросов
- Безопасная передача аргументов навигации
- Правильная очистка ресурсов с Coroutines
- Управление ViewModel, осведомленное о жизненном цикле
- Код, защищенный от нулевых значений, с использованием null safety Kotlin

---

## Заметки разработки

### Запуск тестов

```bash
# Модульные тесты
./gradlew test

# Тесты на устройстве (на эмуляторе или устройстве)
./gradlew connectedAndroidTest
```

### Создание Release APK

```bash
./gradlew assembleRelease
```

### Стиль кода

- Соответствует соглашениям Kotlin
- Лучшие практики Jetpack Compose
- Рекомендации Material 3 Design

---

## Автор

**SkyGet0**

---
