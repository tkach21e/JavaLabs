package Server.Database;

import Common.Model.Coordinates;
import Common.Model.Country;
import Common.Model.Person;
import Common.Model.Product;
import Common.Model.UnitOfMeasure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Stack;

/** Выполняет SQL-запросы, связанные с объектами Product. */
public class ProductRepository {

    private final DatabaseManager databaseManager;

    public ProductRepository(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public Stack<Product> loadAll() throws SQLException {
        String sql = """
                SELECT p.*, u.login AS creator_login FROM lab7_products p
                JOIN lab7_users u ON u.id = p.creator_id
                ORDER BY p.id
                """;
        Stack<Product> products = new Stack<>();
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) products.push(readProduct(resultSet));
        }
        return products;
    }

    public void insert(Product product, String creatorLogin) throws SQLException {
        String sql = """
                INSERT INTO lab7_products (
                    name, coordinate_x, coordinate_y, price, part_number,
                    unit_of_measure, owner_name, owner_birthday, owner_height,
                    owner_weight, owner_nationality, creator_id
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,
                    (SELECT id FROM lab7_users WHERE login = ?))
                RETURNING id, creation_date
                """;

        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            fillProductFields(statement, product);
            statement.setString(12, creatorLogin);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) throw new SQLException("База не вернула созданный id");
                product.setId(resultSet.getLong("id"));
                product.setCreationDate(new Date(resultSet.getTimestamp("creation_date").getTime()));
                product.setCreatorLogin(creatorLogin);
            }
        }
    }

    public boolean update(long id, Product product, String creatorLogin) throws SQLException {
        String sql = """
                UPDATE lab7_products SET
                    name = ?, coordinate_x = ?, coordinate_y = ?, price = ?,
                    part_number = ?, unit_of_measure = ?, owner_name = ?,
                    owner_birthday = ?, owner_height = ?, owner_weight = ?,
                    owner_nationality = ?
                WHERE id = ? AND creator_id =
                    (SELECT id FROM lab7_users WHERE login = ?)
                """;
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            fillProductFields(statement, product);
            statement.setLong(12, id);
            statement.setString(13, creatorLogin);
            return statement.executeUpdate() == 1;
        }
    }

    public boolean deleteById(long id, String creatorLogin) throws SQLException {
        String sql = """
                DELETE FROM lab7_products
                WHERE id = ? AND creator_id =
                    (SELECT id FROM lab7_users WHERE login = ?)
                """;
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.setString(2, creatorLogin);
            return statement.executeUpdate() == 1;
        }
    }

    public int deleteAllOwned(String creatorLogin) throws SQLException {
        String sql = """
                DELETE FROM lab7_products
                WHERE creator_id = (SELECT id FROM lab7_users WHERE login = ?)
                """;
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, creatorLogin);
            return statement.executeUpdate();
        }
    }

    public int deleteGreaterOwned(long price, String creatorLogin) throws SQLException {
        String sql = """
                DELETE FROM lab7_products
                WHERE price > ? AND creator_id =
                    (SELECT id FROM lab7_users WHERE login = ?)
                """;
        try (Connection connection = databaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, price);
            statement.setString(2, creatorLogin);
            return statement.executeUpdate();
        }
    }

    private void fillProductFields(PreparedStatement statement, Product product)
            throws SQLException {
        statement.setString(1, product.getName());
        statement.setDouble(2, product.getCoordinates().getX());
        statement.setFloat(3, product.getCoordinates().getY());
        statement.setLong(4, product.getPrice());
        statement.setString(5, product.getPartNumber());
        statement.setString(6, product.getUnitOfMeasure() == null
                ? null : product.getUnitOfMeasure().name());
        statement.setString(7, product.getOwner().getName());
        statement.setObject(8, product.getOwner().getBirthday());
        statement.setLong(9, product.getOwner().getHeight());
        statement.setObject(10, product.getOwner().getWeight(), java.sql.Types.BIGINT);
        statement.setString(11, product.getOwner().getNationality() == null
                ? null : product.getOwner().getNationality().name());
    }

    private Product readProduct(ResultSet resultSet) throws SQLException {
        Coordinates coordinates = new Coordinates(resultSet.getDouble("coordinate_x"), resultSet.getFloat("coordinate_y"));
        Long weight = resultSet.getObject("owner_weight", Long.class);
        String nationality = resultSet.getString("owner_nationality");
        String unitOfMeasure = resultSet.getString("unit_of_measure");
        Person owner = new Person(
                resultSet.getString("owner_name"),
                resultSet.getObject("owner_birthday", java.time.LocalDate.class),
                resultSet.getLong("owner_height"),
                weight,
                nationality == null ? null : Country.valueOf(nationality));
        Timestamp timestamp = resultSet.getTimestamp("creation_date");
        return new Product(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                coordinates,
                new Date(timestamp.getTime()),
                resultSet.getLong("price"),
                resultSet.getString("part_number"),
                unitOfMeasure == null ? null : UnitOfMeasure.valueOf(unitOfMeasure),
                owner,
                resultSet.getString("creator_login"));
    }

}
