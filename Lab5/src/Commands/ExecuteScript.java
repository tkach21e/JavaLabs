package Commands;

import Managers.CommandManager;
import Managers.InputManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Команда выполнения скрипта из файла.
 *
 * Считывает команды из файла и выполняет их последовательно.
 */
public class ExecuteScript implements Command {

    private final CommandManager commandManager;
    private final Set<String> executingScripts = new HashSet<>();

    public ExecuteScript(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public String getName() {
        return "execute_script";
    }
    @Override
    public String getDescription() {
        return "выполнить команды из файла";
    }
    @Override
    public void execute(String[] args) {

        if (args.length < 2) {
            System.out.println("Ошибка: не указано имя файла.");
            return;
        }

        String fileName = args[1];
        if (executingScripts.contains(fileName)) {
            System.out.println("Ошибка: обнаружена рекурсия выполнения скрипта.");
            return;
        }

        File file = new File(fileName);
        executingScripts.add(fileName);
        InputManager oldInput = commandManager.getInputManager();

        try (Scanner fileScanner = new Scanner(file)) {
            InputManager scriptInput = new InputManager(fileScanner);
            commandManager.setInputManager(scriptInput);
            while (fileScanner.hasNextLine()) {

                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                System.out.println("> " + line);
                try {
                    commandManager.execute(line);
                } catch (Exception e) {
                    System.out.println("Ошибка выполнения команды: " + e.getMessage());
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        } finally {
            commandManager.setInputManager(oldInput);
            executingScripts.remove(fileName);
        }
    }
}