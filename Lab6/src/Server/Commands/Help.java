package Server.Commands;

import Common.Network.Request;
import Common.Network.Response;
import Server.Managers.CommandManager;

/**
 * Обработчик команды HELP.
 * Выводит справку по доступным командам.
 */
public class Help implements Command {

    private CommandManager commandManager;

    public Help(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public String getName() {
        return "help";
    }
    @Override
    public String getDescription() {
        return "вывести справку по доступным командам";
    }

    /**
     * Выполняет команду help - выводит список всех доступных команд.
     *
     * @param request запрос
     * @return ответ со справкой
     */
    @Override
    public Response execute(Request request){

        StringBuilder response = new StringBuilder("Доступные команды:\n");
        commandManager.getCommands().forEach((string, command) ->
                response.append(command.getName()).append(" - ")
                        .append(command.getDescription()).append("\n"));

    return new Response(response.toString());
    }
}
