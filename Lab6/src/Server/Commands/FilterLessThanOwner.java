package Server.Commands;

import Common.Model.Product;
import Common.Network.Request;
import Common.Network.Response;
import Server.Managers.CollectionManager;
import java.util.stream.Collectors;

/**
 * Обработчик команды FILTER_LESS_THAN_OWNER.
 * Фильтрует элементы, чей владелец лексикографически меньше заданного.
 */
public class FilterLessThanOwner implements Command {

    private final CollectionManager collectionManager;

    public FilterLessThanOwner(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "filter_less_than_owner";
    }

    @Override
    public String getDescription() {
        return "показать элементы, владелец которых меньше заданного";
    }

    /**
     * Выполняет команду filter_less_than_owner - фильтрует элементы,
     * чей владелец лексикографически меньше заданного.
     *
     * @param request запрос с именем владельца
     * @return ответ с отфильтрованными элементами
     */
    @Override
    public Response execute(Request request) {

        String owner = request.getStringArgument();

        if (owner == null || owner.isBlank()) {
            return new Response("Имя владельца не передано.");
        }

        String result = collectionManager
                        .getCollection()
                        .stream()
                        .filter(product -> product.getOwner().getName().compareTo(owner) < 0)
                        .sorted(Product::compareTo)
                        .map(Product::toString)
                        .collect(Collectors.joining("\n"));

        if (result.isBlank()) {
            return new Response("Совпадений не найдено.");
        }

        return new Response(result);
    }
}
