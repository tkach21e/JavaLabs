package Server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Common.Network.*;
import Server.Commands.*;
import Server.Managers.*;
import Server.Modules.*;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Server.Database.*;

public class ServerMain {

    private static final Logger logger = LoggerFactory.getLogger(ServerMain.class);
    private static final int DEFAULT_PORT = 4235;
    private static final int PROCESSING_THREADS = 4;
    private static final int SENDING_THREADS = 2;

    public static void main(String[] args) {

        try {

            int serverPort = parsePort(args);

            DatabaseManager databaseManager = new DatabaseManager();
            databaseManager.initialize();
            UserRepository userRepository = new UserRepository(databaseManager);
            ProductRepository productRepository = new ProductRepository(databaseManager);

            CollectionManager collectionManager = new CollectionManager(productRepository.loadAll());
            logger.info("Коллекция загружена из PostgreSQL. Элементов: {}", collectionManager.size());

            CommandManager commandManager = new CommandManager(userRepository);

            commandManager.register(new Help(commandManager));
            commandManager.register(new Show(collectionManager));
            commandManager.register(new Add(collectionManager, productRepository));
            commandManager.register(new Info(collectionManager));
            commandManager.register(new RemoveById(collectionManager, productRepository));
            commandManager.register(new Clear(collectionManager, productRepository));
            commandManager.register(new Update(collectionManager, productRepository));
            commandManager.register(new AddIfMax(collectionManager, productRepository));
            commandManager.register(new RemoveGreater(collectionManager, productRepository));
            commandManager.register(new FilterByOwner(collectionManager));
            commandManager.register(new FilterGreaterThanPartNumber(collectionManager));
            commandManager.register(new Shuffle(collectionManager));
            commandManager.register(new FilterLessThanOwner(collectionManager));

            ConnectionReceiver receiver = new ConnectionReceiver(serverPort);
            logger.info("Сервер запущен на порту: {}", receiver.getPort());

            ExecutorService readingPool = Executors.newCachedThreadPool();
            ExecutorService processingPool = Executors.newFixedThreadPool(PROCESSING_THREADS);
            ExecutorService sendingPool = Executors.newFixedThreadPool(SENDING_THREADS);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Завершение работы сервера...");
                readingPool.shutdownNow();
                processingPool.shutdownNow();
                sendingPool.shutdownNow();
                try {
                    receiver.close();
                } catch (Exception exception) {
                    logger.warn("Не удалось закрыть сетевой канал", exception);
                }
                logger.info("Данные уже сохранены в PostgreSQL. Сервер остановлен.");
            }));

            Thread consoleThread = new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNextLine()) {
                    String input = scanner.nextLine().trim().toLowerCase();
                    if (input.isEmpty()) continue;
                    if (input.equals("exit")) {
                        logger.info("Получена серверная команда exit.");
                        System.exit(0);
                    } else {
                        logger.warn("Неизвестная серверная команда: {}", input);
                    }
                }
            });
            consoleThread.setDaemon(true);
            consoleThread.start();

            while (true) {

                readingPool.execute(() -> {
                    try {
                        RequestData data = RequestReader.read(receiver.getChannel());
                        if (data == null) return;
                        processingPool.execute(() -> {
                            Response response = commandManager.execute(data.getRequest());
                            sendingPool.execute(() -> {
                                try {
                                    ResponseSender.send(
                                            receiver.getChannel(),
                                            response,
                                            data.getClientAddress());
                                } catch (Exception exception) {
                                    logger.error("Ошибка отправки ответа", exception);
                                }
                            });
                        });
                    } catch (Exception exception) {
                        logger.error("Ошибка чтения запроса", exception);
                    }
                });
                Thread.sleep(10);
            }

        } catch (Exception e) {

            logger.error("Критическая ошибка в работе сервера", e);
            System.exit(1);
        }
    }

    private static int parsePort(String[] args) {
        if (args.length == 0) return DEFAULT_PORT;
        try {
            int port = Integer.parseInt(args[0]);
            if (port < 1 || port > 65535) throw new NumberFormatException();
            return port;
        } catch (NumberFormatException exception) {
            logger.warn("Некорректный порт '{}'. Используется {}.", args[0], DEFAULT_PORT);
            return DEFAULT_PORT;
        }
    }
}
