package Commands;

import Managers.CollectionManager;
import Model.Product;


/**
 * Выводит элементы с partNumber больше заданного.
 */
public class FilterGreaterThanPartNumber implements Command {

    private final CollectionManager collectionManager;

    public FilterGreaterThanPartNumber(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName(){
        return "filter_greater_than_part_number";
    }
    @Override
    public String getDescription(){
        return "вывести элементы, значение поля partNumber которых больше заданного";
    }
    @Override
    public void execute(String[] args){
        if (args.length < 2){
            System.out.println("Ошибка: укажите partNumber");
        }
        String partNumber = args[1];
        for (Product product : collectionManager.getAll()){
            if (product.getPartNumber() != null
                    && product.getPartNumber().compareTo(partNumber) > 0){
                System.out.println(product);
            }
        }
    }
}
