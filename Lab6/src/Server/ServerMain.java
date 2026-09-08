package Server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Common.Network.*;
import Server.Commands.*;
import Server.Managers.*;
import Server.Modules.*;

import java.util.Scanner;

public class ServerMain {

    private static final Logger logger = LoggerFactory.getLogger(ServerMain.class);

    public static void main(String[] args) {

        try {

            String fileName = System.getenv("FILE_PATH");
            if (fileName == null || fileName.isBlank()) {
                fileName = "lab6_data.csv";
                logger.info("Переменная окружения FILE_PATH не задана. Используется файл по умолчанию: {}", fileName);
            } else {
                logger.info("Выбран файл коллекции: {}", fileName);
            }

            FileManager fileManager = new FileManager(fileName);
            CollectionManager collectionManager = new CollectionManager(fileManager.load());
            logger.info("Коллекция загружена. Количество элементов: {}", collectionManager.size());

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                                logger.info("Завершение работы сервера. Сохранение коллекции...");
                                fileManager.save(collectionManager.getCollection());
                                logger.info("Коллекция сохранена. Сервер остановлен.");
                            }));

            CommandManager commandManager = new CommandManager();

            commandManager.register(new Help(commandManager));
            commandManager.register(new Show(collectionManager));
            commandManager.register(new Add(collectionManager));
            commandManager.register(new Info(collectionManager));
            commandManager.register(new RemoveById(collectionManager));
            commandManager.register(new Clear(collectionManager));
            commandManager.register(new Update(collectionManager));
            commandManager.register(new AddIfMax(collectionManager));
            commandManager.register(new RemoveGreater(collectionManager));
            commandManager.register(new FilterByOwner(collectionManager));
            commandManager.register(new FilterGreaterThanPartNumber(collectionManager));
            commandManager.register(new Shuffle(collectionManager));
            commandManager.register(new FilterLessThanOwner(collectionManager));
            commandManager.register(new Save(collectionManager, fileManager));

            ConnectionReceiver receiver = new ConnectionReceiver(4235);
            logger.info("Сервер запущен на порту: {}", receiver.getPort());

            // Поток для чтения команд сервера из консоли
            Thread consoleThread = new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String input = scanner.nextLine().trim().toLowerCase();
                    if (input.isEmpty()) continue;
                    if (input.equals("save")) {
                        logger.info("Получена серверная команда save. Сохранение...");
                        fileManager.save(collectionManager.getCollection());
                        logger.info("Сохранение завершено.");
                    } else if (input.equals("exit")) {
                        logger.info("Получена серверная команда exit.");
                        System.exit(0);
                    } else {
                        logger.warn("Неизвестная серверная команда: {}", input);
                    }
                }
            });
            consoleThread.setDaemon(true);
            consoleThread.start();

            // Основной цикл обработки клиентских запросов
            while (true) {

                RequestData data = RequestReader.read(receiver.getChannel());

                if (data == null) {
                    Thread.sleep(10);
                    continue;
                }

                logger.debug("Получен запрос от клиента {}: команда {}", data.getClientAddress(), data.getRequest().getCommandName());
                Response response = commandManager.execute(data.getRequest());
                ResponseSender.send(
                        receiver.getChannel(),
                        response,
                        data.getClientAddress());
                logger.debug("Отправлен ответ клиенту {}: {}", data.getClientAddress(), response.getMessage());
            }

        } catch (Exception e) {

            logger.error("Критическая ошибка в работе сервера", e);
        }
    }
}