package edu.school21.repositories;

import edu.school21.models.Product;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductsRepositoryJdbcImpl implements ProductsRepository{
//    private Connection connection;

//    public ProductsRepositoryJdbcImpl(Connection connection){
//        this.connection = connection;
//    }
    private EmbeddedDatabase database;

    public ProductsRepositoryJdbcImpl(EmbeddedDatabase database){
        this.database = database;
    }

    @Override
    public List<Product> findAll() {
        try {
            List<Product> result = new ArrayList<>();
            Statement statement = database.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from product;");
            while (resultSet.next()){
                Product product = new Product(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("price")
                );
                result.add(product);
            }
            return result;
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public Optional<Product> findById(Long id) {
        try {
            Statement statement = database.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("select * from product where id = " + id + ";");
            if (resultSet.next()){
                return Optional.of(new Product(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("price")
                ));
            } else {
                throw new SQLException();
            }
        } catch (SQLException e){
            return Optional.empty();
        }
    }

    @Override
    public void update(Product product) {
        try {
            PreparedStatement statement = database.getConnection().prepareStatement(
                    "update product set name = ?, price = ? where id = ?;"
            );
            statement.setString(1, product.getName());
            statement.setInt(2, product.getPrice());
            statement.setLong(3, product.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Product product) {
        try {
            PreparedStatement statement = database.getConnection().prepareStatement(
                    "insert into product(name, price) values(?, ?);"
            );
            statement.setString(1, product.getName());
            statement.setInt(2,  product.getPrice());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            PreparedStatement statement = database.getConnection().prepareStatement(
                    "delete from product where id = ?;"
            );
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
