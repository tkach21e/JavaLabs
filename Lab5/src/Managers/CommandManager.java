package Managers;

import Commands.Command;

import java.util.HashMap;
import java.util.Map;


/**
 * Управляет выполнением команд.
 *
 * Хранит список доступных команд и вызывает их по имени.
 */

public class CommandManager {

    private InputManager inputManager;
    private final Map<String, Command> commands = new HashMap<>();

    /**
     * Регистрирует команду
     */
    public void register(Command command) {
        commands.put(command.getName(), command);
    }

    /**
     * Выполняет команду по строке ввода
     */
    public void execute(String input) {
        if (input == null || input.trim().isEmpty()) {
            System.out.println("Введите команду.");
            return;
        }

        String[] parts = input.trim().split("\\s+");
        String commandName = parts[0];
        Command command = commands.get(commandName);

        if (command == null) {
            System.out.println("Неизвестная команда: " + commandName);
            return;
        }

        try {
            command.execute(parts);
        } catch (Exception e) {
            System.out.println("Ошибка при выполнении команды: " + e.getMessage());
        }
    }

    /**
     * Возвращает все команды (для help)
     */
    public Map<String, Command> getCommands() {
        return commands;
    }

    public InputManager getInputManager() {
        return this.inputManager;
    }

    public void setInputManager(InputManager scriptInput) {
        this.inputManager = scriptInput;
    }
}