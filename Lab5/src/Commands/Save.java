package Commands;

import Managers.CollectionManager;
import Managers.FileManager;

/**
 * Команда сохранения коллекции в файл.
 */
public class Save implements Command {

    private final CollectionManager collectionManager;
    private final FileManager fileManager;

    public Save(CollectionManager collectionManager, FileManager fileManager) {
        this.collectionManager = collectionManager;
        this.fileManager = fileManager;
    }

    @Override
    public String getName() {
        return "save";
    }
    @Override
    public String getDescription() {
        return "сохранить коллекцию в файл";
    }
    @Override
    public void execute(String[] args) {
        fileManager.save(collectionManager.getAll());
    }
}