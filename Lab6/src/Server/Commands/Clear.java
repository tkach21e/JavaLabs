package Server.Commands;

import Common.Network.Request;
import Common.Network.Response;
import Server.Managers.CollectionManager;

/**
 * Обработчик команды CLEAR.
 * Очищает коллекцию.
 */
public class Clear implements Command {

    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "очистить коллекцию";
    }

    /**
     * Выполняет команду clear - очищает коллекцию.
     *
     * @param request запрос
     * @return ответ с результатом
     */
    @Override
    public Response execute(Request request) {
        collectionManager.clear();
        return new Response("Коллекция очищена.");
    }
}
