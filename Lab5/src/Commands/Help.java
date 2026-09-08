package Commands;

import Managers.CommandManager;

/**
 * Выводит список доступных команд.
 */
public class Help implements Command {

    private final CommandManager commandManager;

    public Help(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public String getName(){
        return "help";
    }
    @Override
    public String getDescription(){
        return "вывести справку по доступным командам";
    }
    @Override
    public void execute(String[] args){
        commandManager.getCommands().values()
                .forEach(command ->
                                System.out.println(command.getName() + " : " + command.getDescription()));
    }
}
