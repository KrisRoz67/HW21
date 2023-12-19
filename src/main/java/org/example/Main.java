package org.example;

import org.example.entity.Customer;
import org.example.entity.Order;
import org.example.entity.Product;
import org.example.repository.CustomerRepository;
import org.example.repository.OrderRepository;
import org.example.repository.ProductRepository;
import org.example.service.CustomerService;
import org.example.service.OrderService;
import org.example.service.ProductService;

public class Main {

    public static void main(String[] args) {
        CustomerRepository customerRepository = new CustomerRepository();
        OrderRepository orderRepository = new OrderRepository();
        ProductRepository productRepository = new ProductRepository();
        CustomerService customerService = new CustomerService(customerRepository);
        OrderService orderService = new OrderService(orderRepository,productRepository);
        ProductService productService = new ProductService(productRepository);
        System.out.println("--------------------");
        System.out.println("Get order by id");
        System.out.println(orderService.getOrderById(1));
        System.out.println("--------------------");
        System.out.println("Update order");
        Order updatedOrder = orderService.updateOrder(new Order(1,1,1));
        System.out.println(orderService.getOrderById(updatedOrder.getId()));
        System.out.println("--------------------");
        System.out.println("Create order");
        Order order = orderService.createOrder(new Order(0,2,6));
        System.out.println("Created order : \n" + orderService.getOrderById(order.getId()));
        System.out.println("--------------------");
        System.out.println(String.format("Delete created order with id %s",order.getId()));
        orderService.deleteOrder(order.getId());
        System.out.println("Try to find deleted order : \n" + orderService.getOrderById(order.getId()));
        System.out.println("--------------------");
        System.out.println("Get customer's orders");
        System.out.println(orderService.getOrdersByCustomer(3));
        System.out.println("--------------------");
        System.out.println("Get total sum of customer's orders");
        System.out.println(orderService.getTotalPriceForCustomer(3));
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("Get customer by id");
        System.out.println(customerService.getById(4));
        System.out.println("--------------------");
        System.out.println("Update customer");
        Customer updatedCustomer = customerService.updateCustomer(new Customer(2,"Updated name"));
        System.out.println("Return updated customer : \n" + customerService.getById(updatedCustomer.getId()));
        System.out.println("--------------------");
        System.out.println("Create customer");
        Customer customer = customerService.createCustomer(new Customer(0,"New customer"));
        System.out.println("Created customer : \n" + customerService.getById(customer.getId()));
        System.out.println("--------------------");
        System.out.println(String.format("Delete created customer with id %s",customer.getId()));
        customerService.deleteCustomer(customer.getId());
        System.out.println("Try to find deleted customer : \n" +customerService.getById(customer.getId()));
        System.out.println("--------------------");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("Get product by id");
        System.out.println(productService.getById(2));
        System.out.println("--------------------");
        System.out.println("Update product");
        Product updatedProduct = productService.updateProduct(new Product(2,"Updated product",12.23));
        System.out.println("Return updated product : \n" + productService.getById(updatedProduct.getId()));
        System.out.println("--------------------");
        System.out.println("Create product");
        Product product= productService.createProduct(new Product(0,"New product",12.23));
        System.out.println("Created product : \n" +productService.getById(product.getId()));
        System.out.println("--------------------");
        System.out.println(String.format("Delete created product with id %s",product.getId()));
        productService.deleteProduct(product.getId());
        System.out.println("Try to find deleted customer : \n" +productService.getById(product.getId()));
        System.out.println("--------------------");

    }
}
