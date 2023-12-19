package org.example.repository;

import org.example.DataBaseConnection;
import org.example.entity.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepository implements CrudRepository<Order> {
    @Override
    public Order findById(int id) {
        try {
            Connection connection = DataBaseConnection.getINSTANCE().getConnection();
            String sql = "SELECT * FROM customer_order WHERE id= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt("id"));
                order.setCustomerId(resultSet.getInt("customer_id"));
                order.setProductId(resultSet.getInt("product_id"));
                return order;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Order create(Order entity) {
        Connection connection = DataBaseConnection.getINSTANCE().getConnection();
        String sql = "INSERT INTO customer_order(customer_id,product_id) VALUES ( ?,?)";
            try {
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, entity.getCustomerId());
                preparedStatement.setInt(2, entity.getProductId());
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
            String sql = "DELETE FROM customer_order WHERE id= ?";
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
    public Order update(Order entity) {
        Connection connection = DataBaseConnection.getINSTANCE().getConnection();
        try {
            connection.setAutoCommit(false);
            String sql = "UPDATE customer_order SET product_id=?,customer_id=? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, entity.getProductId());
            preparedStatement.setInt(2, entity.getCustomerId());
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
    public List<Order> getOrdersByCustomer(int customerId) {
        try {
            Connection connection = DataBaseConnection.getINSTANCE().getConnection();
            String sql = "SELECT * FROM customer_order WHERE customer_id= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Order> orders = new ArrayList<>();

            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt("id"));
                order.setCustomerId(resultSet.getInt("customer_id"));
                order.setProductId(resultSet.getInt("product_id"));
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
