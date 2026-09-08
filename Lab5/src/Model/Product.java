package Model;

import Managers.IdGenerator;

import java.util.Date;

/**
 * Класс Product представляет объект, хранящийся в коллекции.
 *
 * Содержит информацию о продукте, включая:
 * - уникальный идентификатор
 * - название
 * - координаты
 * - цену
 * - владельца
 *
 * Реализует интерфейс Comparable для сортировки элементов.
 */

public class Product implements Comparable<Product>{
    private long id;                     //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name;                 //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates;     //Поле не может быть null
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long price;                  //Поле не может быть null, Значение поля должно быть больше 0
    private String partNumber;           //Строка не может быть пустой, Поле может быть null
    private UnitOfMeasure unitOfMeasure; //Поле может быть null
    private Person owner;                 //Поле не может быть null

    public Product(String name,
                   Coordinates coordinates,
                   Long price,
                   String partNumber,
                   UnitOfMeasure unitOfMeasure,
                   Person owner){
        this.id = IdGenerator.generateId(); // генерируется автоматически
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = new Date(); // генерируется автоматически
        this.price = price;
        this.partNumber = partNumber;
        this.unitOfMeasure = unitOfMeasure;
        this.owner = owner;
    }

    public Product(long id,
                   String name,
                   Coordinates coordinates,
                   Long price,
                   String partNumber,
                   UnitOfMeasure unitOfMeasure,
                   Person owner) {
        this(name, coordinates, price, partNumber, unitOfMeasure, owner);
        this.id = id;
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
