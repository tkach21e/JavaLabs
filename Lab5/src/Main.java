import Commands.*;
import Managers.*;
import Managers.InputManager;

import java.io.File;
import java.util.Scanner;


/**
 * Точка входа в приложение.
 *
 * Инициализирует основные компоненты программы:
 * - менеджер коллекции
 * - менеджер команд
 * - менеджер ввода
 *
 * Запускает интерактивный режим работы программы.
 */

public class Main {

    public static void main(String[] args) {

        // Получаем имя файла из переменной окружения
        String path = System.getenv("FILE_PATH");
        if (path == null){
            path = "lab5_data.csv";
            System.out.println("Переменная окружения не задана. Использован файл по умолчанию (lab5_data.csv)");
        } else {
            System.out.println("Задана переменная окружения: " + path);
        }

        File file = new File(path);
        if (!file.exists()){
            System.out.println("Не удается найти указанный файл.");
            System.exit(0);
        }

        // Инициализация основных компонентов
        Scanner scanner = new Scanner(System.in);
        CollectionManager collectionManager = new CollectionManager();
        CommandManager commandManager = new CommandManager();
        InputManager inputManager = new InputManager(scanner);
        commandManager.setInputManager(inputManager);
        FileManager fileManager = new FileManager(path);

        // Загрузка данных из файла
        collectionManager.getAll().addAll(fileManager.load());

        // Регистрация команд
        commandManager.register(new Help(commandManager));
        commandManager.register(new Show(collectionManager));
        commandManager.register(new Add(collectionManager, commandManager));
        commandManager.register(new Info(collectionManager));
        commandManager.register(new RemoveById(collectionManager));
        commandManager.register(new Clear(collectionManager));
        commandManager.register(new Update(collectionManager, commandManager));
        commandManager.register(new AddIfMax(collectionManager, commandManager));
        commandManager.register(new RemoveGreater(collectionManager, commandManager));
        commandManager.register(new FilterByOwner(collectionManager));
        commandManager.register(new Save(collectionManager, fileManager));
        commandManager.register(new ExecuteScript(commandManager));
        commandManager.register(new FilterGreaterThanPartNumber(collectionManager));
        commandManager.register(new Shuffle(collectionManager));
        commandManager.register(new FilterLessThanOwner(collectionManager));
        Exit exitCommand = new Exit();
        commandManager.register(exitCommand);

        // Основной цикл
        System.out.println("Программа запущена.");
        while (true) {
            try {
                System.out.print("Введите команду> ");
                String input = scanner.nextLine();

                commandManager.execute(input);

                if (exitCommand.shouldExit()) {
                    break;
                }

            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }

        scanner.close();
        System.out.println("Программа завершена.");
    }
}
