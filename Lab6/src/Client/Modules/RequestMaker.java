package Client.Modules;

import Client.Managers.InputManager;
import Common.Network.Request;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Преобразует строки, введённые пользователем, в Request объекты.
 */
public class RequestMaker {

    private final InputManager inputManager;
    static Set<String> executingScripts = new HashSet<>();


    public RequestMaker(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    /**
     * Возвращает Request объект.
     *
     * @param line введённая строка команды
     * @return объект Request или null если команда не распознана
     */
    public Request make(String line) {

        String[] parts = line.trim().split("\\s+");
        String command = parts[0].toLowerCase();

        switch (command) {

            case "help":
                return new Request("help");

            case "info":
                return new Request("info");

            case "show":
                return new Request("show");

            case "clear":
                return new Request("clear");

            case "add":
                Request request = new Request("add");
                request.setProduct(inputManager.readProduct());
                return request;

            case "remove_by_id":
                if (parts.length != 2) {
                    System.out.println("Некорректный ввод команды: remove_by_id <id>");
                    return new Request("undefined");
                }
                try {

                    long id = Long.parseLong(parts[1]);
                    Request request1 = new Request("remove_by_id");
                    request1.setId(id);
                    return request1;

                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: id должен быть числом.");
                    return new Request("undefined");
                }

            case "update":
                if (parts.length != 2) {
                    System.out.println("Некорректный ввод команды: update <id>");
                    return new Request("undefined");
                }
                try {

                    long id = Long.parseLong(parts[1]);
                    Request request2 = new Request("update");
                    request2.setId(id);
                    request2.setProduct(inputManager.readProduct());
                    return request2;

                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: id должен быть числом.");
                    return new Request("undefined");
                }

            case "filter_by_owner":
                if (parts.length != 2) {
                    System.out.println("Некорректный ввод команды: filter_by_owner <owner>");
                    return new Request("undefined");
                }
                Request request3 = new Request("filter_by_owner");
                request3.setStringArgument(parts[1]);
                return request3;

            case "filter_greater_than_part_number":
                if (parts.length != 2) {
                    System.out.println("Некорректный ввод команды: filter_greater_than_part_number <partNumber>");
                    return new Request("undefined");
                }
                Request request4 = new Request("filter_greater_than_part_number");
                request4.setStringArgument(parts[1]);
                return request4;

            case "shuffle":
                return new Request("shuffle");

            case "add_if_max":
                Request request5 = new Request("add_if_max");
                request5.setProduct(inputManager.readProduct());
                return request5;

            case "remove_greater":
                Request request6 = new Request("remove_greater");
                request6.setProduct(inputManager.readProduct());
                return request6;

            case "filter_less_than_owner":
                if (parts.length != 2) {
                    System.out.println("Некорректный ввод команды: filter_less_than_owner <owner>");
                    return new Request("undefined");
                }
                Request request7 = new Request("filter_less_than_owner");
                request7.setStringArgument(parts[1]);
                return request7;

            case "save":
                System.out.println("Команда 'save' доступна только серверу.");
                return new Request("undefined");

            case "execute_script":
                if (parts.length != 2) {
                    System.out.println("Некорректный ввод: execute_script <filename>");
                    return new Request("undefined");
                }
                File file = new File(parts[1]);
                if (!file.exists()) {
                    System.out.println("Файл не найден: " + parts[1]);
                    return new Request("undefined");
                }
                try {
                    String scriptPath = file.getCanonicalPath();
                    Request requestScript = new Request("execute_script");
                    requestScript.setStringArgument(scriptPath);
                    return requestScript;   // Будет обработан локально в ClientMain
                } catch (IOException e) {
                    System.out.println("Ошибка пути: " + e.getMessage());
                    return new Request("undefined");
                }

            default:
                return null;
        }
    }

    public static boolean tryStartScript(String path) {
        return executingScripts.add(path);
    }

    public static void endScript(String path) {
        executingScripts.remove(path);
    }
}
