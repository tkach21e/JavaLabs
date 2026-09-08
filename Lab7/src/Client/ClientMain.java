package Client;

import Client.Managers.ScannerManager;
import Client.Managers.InputManager;
import Client.Modules.RequestMaker;
import Client.Modules.RequestSender;
import Common.Network.Request;
import Common.Network.Response;

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
    private static int serverPort = 4235;
    private static String login;
    private static String password;

    public static void main(String[] args) {
        try {
            run(args);
        } catch (NoSuchElementException exception) {
            System.out.println("Ввод завершён. Клиент завершён.");
        }
    }

    private static void run(String[] args) {

        if (args.length > 0) {
            serverHost = args[0];
        }
        if (args.length > 1) {
            try {
                serverPort = Integer.parseInt(args[1]);
                if (serverPort < 1 || serverPort > 65535) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("Порт должен быть числом от 1 до 65535.");
                return;
            }
        }

        ScannerManager scannerManager = new ScannerManager();
        InputManager inputManager = new InputManager(scannerManager.getScanner());
        RequestMaker requestMaker = new RequestMaker(inputManager);

        System.out.println("Клиент запущен. Сервер: " + serverHost + ":" + serverPort);
        if (!authenticate(scannerManager.getScanner())) {
            System.out.println("Клиент завершён.");
            return;
        }
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
            request.setCredentials(login, password);

            if (request.getCommandName().equals("execute_script")) {
                runScript(request.getStringArgument());
                continue;
            }

            Response response = RequestSender.sendRequest(serverHost, serverPort, request);
            System.out.println(response.getMessage());
        }
    }

    private static boolean authenticate(Scanner scanner) {
        while (true) {
            System.out.println("\n1 — войти");
            System.out.println("2 — зарегистрироваться");
            System.out.println("3 — завершить работу");
            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine().trim();
            if (choice.equalsIgnoreCase("3")) return false;
            if (!choice.equals("1") && !choice.equals("2")) {
                System.out.println("Введите 1, 2 или 3.");
                continue;
            }

            System.out.print("Логин: ");
            String enteredLogin = scanner.nextLine().trim();
            System.out.print("Пароль: ");
            String enteredPassword = scanner.nextLine().trim();
            Request request = new Request(choice.equals("1") ? "login" : "register");
            request.setCredentials(enteredLogin, enteredPassword);
            Response response = RequestSender.sendRequest(serverHost, serverPort, request);
            System.out.println(response.getMessage());
            if (response.isSuccess()) {
                login = enteredLogin;
                password = enteredPassword;
                return true;
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
                if (cmd.equals("exit")) {
                    System.out.println("Команда exit в скрипте. Клиент завершается.");
                    System.exit(0);
                }

                Request scRequest = scRequestMaker.make(line);
                if (scRequest == null) {
                    System.out.println("Неизвестная команда в скрипте: " + line);
                    continue;
                }
                if (scRequest.getCommandName().equals("undefined")) continue;
                scRequest.setCredentials(login, password);

                if (scRequest.getCommandName().equals("execute_script")) {
                    runScript(scRequest.getStringArgument());
                    continue;
                }

                Response response = RequestSender.sendRequest(serverHost, serverPort, scRequest);
                System.out.println(response.getMessage());

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
