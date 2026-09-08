package Server.Commands;

import Common.Network.Request;
import Common.Network.Response;
import Server.Managers.CollectionManager;

/**
 * Обработчик команды SHUFFLE.
 * Перемешивает элементы коллекции в случайном порядке.
 */
public class Shuffle implements Command {

    private final CollectionManager collectionManager;

    public Shuffle(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "shuffle";
    }

    @Override
    public String getDescription() {
        return "перемешать всю коллекцию в памяти, не меняя данные объектов";
    }

    /**
     * Выполняет команду shuffle - перемешивает элементы в случайном порядке.
     *
     * @param request запрос
     * @return ответ с результатом
     */
    @Override
    public Response execute(Request request) {

        collectionManager.shuffle();
        return new Response("Элементы коллекции перемешаны.");
    }
}
