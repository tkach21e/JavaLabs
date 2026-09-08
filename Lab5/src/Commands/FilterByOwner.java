package Commands;

import Managers.CollectionManager;
import Model.Product;


/**
 * Фильтрует элементы по владельцу.
 */
public class FilterByOwner implements Command {

     private final CollectionManager collectionManager;

     public FilterByOwner(CollectionManager collectionManager){
         this.collectionManager = collectionManager;
     }

     @Override
    public String getName(){
         return "filter_by_owner";
     }
     @Override
    public String getDescription(){
         return "показать элементы соответствующего владельца";
     }
     @Override
    public void execute(String[] args){
         if (args.length < 2){
             System.out.println("Ошибка: укажите имя владельца");
             return;
         }
         String ownerName = args[1];
         for (Product product : collectionManager.getAll()){
             if (product.getOwner().getName().equals(ownerName)){
                 System.out.println(product);
             }
         }
     }
}
