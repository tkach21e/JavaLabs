package Commands;

import Managers.CollectionManager;


/**
 * Команда очистки коллекции.
 */
public class Clear implements Command {

    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName(){
        return "clear";
    }
    @Override
    public String getDescription(){
        return "очистить коллекцию";
    }
    @Override
    public void execute(String[] args){
        collectionManager.clear();
        System.out.println("Коллекция очищена.");
    }
}
