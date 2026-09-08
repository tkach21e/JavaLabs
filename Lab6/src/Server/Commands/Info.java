package Server.Commands;

import Common.Network.Request;
import Common.Network.Response;
import Server.Managers.CollectionManager;

/**
 * Обработчик команды INFO.
 * Выводит информацию о коллекции.
 */
public class Info implements Command {

    private final CollectionManager collectionManager;

    public Info(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "вывести информацию о коллекции";
    }

    /**
     * Выполняет команду info - выводит информацию о коллекции.
     *
     * @param request запрос
     * @return ответ с информацией
     */
    @Override
    public Response execute(Request request) {

        String result = "Тип коллекции: "
                        + collectionManager.getCollection().getClass().getSimpleName()
                        + "\nКоличество элементов: "
                        + collectionManager.getCollection().size()
                        + "\nДата инициализации: "
                        + collectionManager.getInitializationDate();

        return new Response(result);
    }
}
