package Server.Commands;

import Common.Model.Product;
import Common.Network.Request;
import Common.Network.Response;
import Server.Managers.CollectionManager;
import java.util.stream.Collectors;

/**
 * Обработчик команды FILTER_BY_OWNER.
 * Фильтрует элементы по имени владельца.
 */
public class FilterByOwner implements Command {

    private final CollectionManager collectionManager;

    public FilterByOwner(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName() {
        return "filter_by_owner";
    }

    @Override
    public String getDescription() {
        return "показать элементы соответствующего владельца";
    }

    /**
     * Выполняет команду filter_by_owner - фильтрует элементы по владельцу.
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
                        .filter(product -> product.getOwner().getName().equals(owner))
                        .sorted(Product::compareTo)
                        .map(Product::toString)
                        .collect(Collectors.joining("\n"));

        if (result.isBlank()) {
            return new Response("Совпадений не найдено.");
        }
        
        return new Response(result);
    }
}
