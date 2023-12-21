package org.example.repository;

import org.example.DataBaseConnection;
import org.example.entity.Order;
import org.example.entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRepository implements CrudRepository<Product> {
    @Override
    public Product findById(int id) {
        try {
            Connection connection = DataBaseConnection.getINSTANCE().getConnection();
            String sql = "SELECT * FROM product WHERE id= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
               Product product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setPrice(resultSet.getDouble("price"));
                return product;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Product create(Product entity) {
        Connection connection = DataBaseConnection.getINSTANCE().getConnection();
        String sql = "INSERT INTO product (name,price) VALUES ( ?,?)";
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setDouble(2, entity.getPrice());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows <= 1) {
                connection.commit();
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getInt(1));
                    }
                }

            } else {
                connection.rollback();
            }
            return entity;
        } catch (SQLException e) {
            rollback(connection, e);
        } finally {
            setAutoCommitStatus(connection, true);
        }
        return null;
    }

    @Override
    public void delete(int id) {
        Connection connection = DataBaseConnection.getINSTANCE().getConnection();
        try {
            setAutoCommitStatus(connection, false);
            String sql = "DELETE FROM product WHERE id= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows <= 1) {
                connection.commit();
            } else {
                connection.rollback();
            }
        } catch (SQLException e) {
            rollback(connection, e);
        } finally {
            setAutoCommitStatus(connection, true);
        }

    }

    @Override
    public Product update(Product entity) {
        Connection connection = DataBaseConnection.getINSTANCE().getConnection();
        try {
            connection.setAutoCommit(false);
            String sql = "UPDATE product  SET name=?,price=? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setDouble(2, entity.getPrice());
            preparedStatement.setInt(3, entity.getId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows <= 1) {
                connection.commit();
            } else {
                connection.rollback();
            }
            return entity;
        } catch (SQLException e) {
            rollback(connection, e);
        } finally {
            setAutoCommitStatus(connection, true);
        }
        return null;
    }

    public void rollback(Connection connection, SQLException e) {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException(e);
    }

    public void setAutoCommitStatus(Connection connection, boolean status) {
        try {
            connection.setAutoCommit(status);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
