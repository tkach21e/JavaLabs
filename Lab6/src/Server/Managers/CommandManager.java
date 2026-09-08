package Server.Managers;

import Common.Network.*;
import Server.Commands.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Управляет командами и их обработчиками.
 * Отвечает за:
 * - регистрацию обработчиков команд
 * - маршрутизацию команд к соответствующим обработчикам
 * - выполнение команд
 */
public class CommandManager {

    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Выполняет команду на основе запроса.
     *
     * @param request запрос с типом и параметрами команды
     * @return ответ с результатом выполнения
     */
    public Response execute(Request request) {
        Command command = commands.get(request.getCommandName());
        if (command == null) {
            return new Response("На сервере нет этой команды.");
        }
        return command.execute(request);
    }

    /**
     * Регистрирует команду
     */
    public void register(Command command) {
        commands.put(command.getName(), command);
    }

    /**
     * Возвращает все команды
     */
    public Map<String, Command> getCommands() {
        return commands;
    }
}
