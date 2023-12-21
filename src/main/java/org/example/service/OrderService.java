package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Order;
import org.example.entity.Product;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;


    /**
     * Получить заказ по его уникальному идентификатору.
     *
     * @param id Уникальный идентификатор заказа.
     * @return {@link Optional}, содержащий заказ, если найден, или пустой {@link Optional}, если не найден.
     */
    public Optional<Order> getOrderById(int id) {
     return Optional.ofNullable(orderRepository.findById(id));
    }

    /**
     * Получить список заказов, связанных с конкретным клиентом.
     *
     * @param customerId Уникальный идентификатор клиента.
     * @return Список заказов, связанных с клиентом.
     */
    public List<Order> getOrdersByCustomer(int customerId) {
       return orderRepository.getOrdersByCustomer(customerId);
    }

    /**
     * Рассчитать общую стоимость всех заказов для конкретного клиента.
     *
     * @param customerId Уникальный идентификатор клиента.
     * @return Общая стоимость всех заказов для клиента.
     */
    public double getTotalPriceForCustomer(int customerId) {
        double sum = 0;
        List<Order> orders = getOrdersByCustomer(customerId);
        for (Order order : orders) {
            Product product = productRepository.findById(order.getProductId());
            sum += product.getPrice();
        }
     return sum;
    }

    /**
     * Создать новый заказ и добавить его в репозиторий.
     *
     * @param order Заказ, который нужно создать и добавить.
     * @throws IllegalArgumentException Если заказ уже существует в репозитории.
     */
    public Order createOrder(Order order) {
        if(getOrderById(order.getId()).isPresent()) {
            throw new IllegalArgumentException("Order with this id already exists");
        }
        return orderRepository.create(order);

    }

    /**
     * Обновить данные заказа.
     *
     * @param order Клиент, которого нужно создать и добавить.
     * @throws IllegalArgumentException Если клиент с таким идентификатором не существует или обновленный объект не проходит валидацию.
     */
    public Order updateOrder(Order order) {
        if(getOrderById(order.getId()).isEmpty()) {
            throw new IllegalArgumentException("Order with this id doesn't exist");
        }
        return orderRepository.update(order);
    }

    /**
     * Удалить заказ по его уникальному идентификатору.
     *
     * @param orderId Уникальный идентификатор заказа, который нужно удалить.
     * @throws IllegalArgumentException Если заказ с указанным идентификатором не существует в репозитории.
     */
    public void deleteOrder(int orderId) {
       if(getOrderById(orderId).isEmpty()) {
           throw new IllegalArgumentException(String.format("Order with this id %s doesn't exist", orderId));
       }
       orderRepository.delete(orderId);

       }
    }