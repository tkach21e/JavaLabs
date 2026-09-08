package Client;

import Client.Managers.ScannerManager;
import Client.Managers.InputManager;
import Client.Modules.RequestMaker;
import Client.Modules.RequestSender;
import Common.Network.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Главный класс клиентского приложения.
 * Отвечает за:
 * - инициализацию компонентов клиента
 * - чтение команд из консоли
 * - парсинг и отправку команд на сервер
 * - вывод результатов
 */
public class ClientMain {

    private static String serverHost = "localhost";
    private static int serverPort = 5555;

    public static void main(String[] args) {

        // Парсинг аргументов: java -jar client.jar <host> [port]
        if (args.length > 0) {
            serverHost = args[0];
        }
        if (args.length > 1) {
            try {
                serverPort = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }

        ScannerManager scannerManager = new ScannerManager();
        InputManager inputManager = new InputManager(scannerManager.getScanner());
        RequestMaker requestMaker = new RequestMaker(inputManager);

        System.out.println("Клиент запущен. Сервер: " + serverHost + ":" + serverPort);
        System.out.println("Введите 'help' для справки или 'exit' для выхода.");

        while (true) {

            String line = scannerManager.readCommand();
            if (line.isBlank()) continue;
            if (line.equalsIgnoreCase("exit")) {
                System.out.println("Клиент завершён.");
                break;
            }

            Request request = requestMaker.make(line);
            if (request == null) {
                System.out.println("Неизвестная команда. Введите 'help' для справки.");
                continue;
            }
            if (request.getCommandName().equals("undefined")) continue;

            // Локальная обработка execute_script
            if (request.getCommandName().equals("execute_script")) {
                runScript(request.getStringArgument());
                continue;
            }

            try {

                RequestSender.sendRequest(serverHost, serverPort, request);

            } catch (Exception e) {
                System.out.println("Ошибка при отправке команды: " + e.getMessage());
            }
        }
    }

    private static void runScript(String scriptPath) {
        if (!RequestMaker.tryStartScript(scriptPath)) {
            System.out.println("Рекурсия! Скрипт уже выполняется: " + scriptPath);
            return;
        }

        try (Scanner scScanner = new Scanner(new File(scriptPath))) {

            InputManager scInputManager = new InputManager(scScanner);
            RequestMaker scRequestMaker = new RequestMaker(scInputManager);

            while (scScanner.hasNextLine()) {

                String line = scScanner.nextLine().trim();
                if (line.isEmpty()) continue;
                System.out.println("script> " + line);

                String cmd = line.split("\\s+")[0].toLowerCase();
                if (cmd.equals("add")
                        || cmd.equals("update")
                        || cmd.equals("add_if_max")
                        || cmd.equals("remove_greater")) {
                    continue;
                }

                Request scRequest = scRequestMaker.make(line);
                if (scRequest == null) {
                    System.out.println("Неизвестная команда в скрипте: " + line);
                    continue;
                }
                if (scRequest.getCommandName().equals("undefined")) continue;

                // Вложенный execute_script
                if (scRequest.getCommandName().equals("execute_script")) {
                    runScript(scRequest.getStringArgument());
                    continue;
                }

                // exit в скрипте
                if (scRequest.getCommandName().equals("exit")) {
                    System.out.println("Команда exit в скрипте. Клиент завершается.");
                    System.exit(0);
                }

                RequestSender.sendRequest(serverHost, serverPort, scRequest);

            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл скрипта не найден: " + scriptPath);
        } catch (NoSuchElementException e) {
            System.out.println("Ошибка в скрипте: не хватает данных для команды.");
        } catch (Exception e) {
            System.out.println("Ошибка выполнения скрипта: " + e.getMessage());
        } finally {
            RequestMaker.endScript(scriptPath);
        }
    }
}
