package ru.masnaviev.habittracker.app.util;


public class ConsoleView {

    public static void displayLoginMenu() {
        System.out.println("""
                1. Регистрация
                2. Вход в систему
                3. Выход
                """);
    }

    public static void displayMainMenu() {
        System.out.println("""
                1. Управление аккаунтом
                2. Управление привычками
                3. Отслеживание выполнения привычек
                4. Статистика и аналитика
                5. Уведомления
                6. Администрирование
                7. Смена пользователя
                8. Выход
                """);
    }

    public static void displayAccountMenu() {
        System.out.println("""
                1. Редактирование профиля
                2. Удаление профиля
                3. Сброс пароля через email
                """);
    }

    public static void displayHabitMenu() {
        System.out.println("""
                1. Создание привычки
                2. Редактирование привычки
                3. Удаление Привычки
                4. Просмотр привычек
                """);
    }

    public static void displayIncorrectChoice() {
        System.out.println("Неверный выбор. Попробуйте снова.");
    }

    public static void displayTrackingMenu() {
        System.out.println("""
                1. Отметка выполнения привычки
                2. Генерация статистики выполнения
                """);
    }

    public static void displayAdministrationMenu() {
        System.out.println("""
                1. Получение всех пользователей
                2. Блокировка пользователя
                3. Разблокировка пользователя
                4. Удаление пользователя
                5. Получение списка всех привычек
                6. Удаление привычки
                """);
    }

    public static void displayNoImplementation(){
        System.out.println("Функциональность еще не добавлена");
    }
}
