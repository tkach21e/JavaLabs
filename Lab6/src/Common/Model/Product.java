package Common.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * Класс Product представляет объект, хранящийся в коллекции.
 * Содержит информацию о продукте, включая:
 * - уникальный идентификатор
 * - название
 * - координаты
 * - цену
 * - владельца
 * Реализует интерфейс Comparable для сортировки элементов.
 */

public class Product implements Comparable<Product>, Serializable {

    private long id;                     //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private final String name;                 //Поле не может быть null, Строка не может быть пустой
    private final Coordinates coordinates;     //Поле не может быть null
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private final Long price;                  //Поле не может быть null, Значение поля должно быть больше 0
    private final String partNumber;           //Строка не может быть пустой, Поле может быть null
    private final UnitOfMeasure unitOfMeasure; //Поле может быть null
    private final Person owner;                 //Поле не может быть null

    public Product(String name,
                   Coordinates coordinates,
                   Long price,
                   String partNumber,
                   UnitOfMeasure unitOfMeasure,
                   Person owner){
        this.name = name;
        this.coordinates = coordinates;
        this.price = price;
        this.partNumber = partNumber;
        this.unitOfMeasure = unitOfMeasure;
        this.owner = owner;
    }

    public Product(long id,
                   String name,
                   Coordinates coordinates,
                   Date creationDate,
                   Long price,
                   String partNumber,
                   UnitOfMeasure unitOfMeasure,
                   Person owner){
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.price = price;
        this.partNumber = partNumber;
        this.unitOfMeasure = unitOfMeasure;
        this.owner = owner;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Long getPrice() {
        return price;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public Person getOwner() {
        return owner;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Сравнивает текущий объект с другим продуктом.
     *
     * @param other другой продукт
     * @return отрицательное число, если меньше; 0 если равны; положительное если больше
     */

    @Override
    public int compareTo(Product other){
        return this.price.compareTo(other.price);
    }

    @Override
    public String toString() {
        return "Product{id=" + id +
                ", name=" + name +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", price=" + price +
                ", partNumber=" + partNumber +
                ", unitOfMeasure=" + unitOfMeasure +
                ", owner=" + owner +
                '}';
    }
}
