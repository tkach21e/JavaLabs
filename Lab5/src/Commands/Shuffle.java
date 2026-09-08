package Commands;

import Managers.CollectionManager;

/**
 * Перемешивает элементы коллекции в случайном порядке.
 */
public class Shuffle implements Command {

    private final CollectionManager collectionManager;

    public Shuffle(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    @Override
    public String getName(){
        return "shuffle";
    }
    @Override
    public String getDescription(){
        return "перемешать элементы коллекции в случайном порядке";
    }
    @Override
    public void execute(String[] args){
        collectionManager.shuffle();
        System.out.println("Элементы перемешаны.");
    }
}
