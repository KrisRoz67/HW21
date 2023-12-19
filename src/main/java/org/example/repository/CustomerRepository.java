package org.example.repository;

import org.example.DataBaseConnection;
import org.example.entity.Customer;
import org.example.entity.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRepository implements CrudRepository<Customer>{
    @Override
    public Customer findById(int id) {
        try {
            Connection connection = DataBaseConnection.getINSTANCE().getConnection();
            String sql = "SELECT * FROM customer WHERE id= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getInt("id"));
                customer.setName(resultSet.getString("name"));
                return customer;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Customer create(Customer entity) {
        Connection connection = DataBaseConnection.getINSTANCE().getConnection();
        String sql = "INSERT INTO customer (name) VALUES ( ?)";
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getName());
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
            String sql = "DELETE FROM customer WHERE id= ?";
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
    public Customer update(Customer entity) {
        Connection connection = DataBaseConnection.getINSTANCE().getConnection();
        try {
            connection.setAutoCommit(false);
            String sql = "UPDATE customer SET name=? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getId());
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
