package com.example.yandex_interview.postalbox.service;

import com.example.yandex_interview.postalbox.entity.Order;
import com.example.yandex_interview.postalbox.enums.OrderStatus;
import com.example.yandex_interview.postalbox.repo.OrderDB;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class OrderService {
    private static final Set<OrderStatus> PERMITTED_ORDER_STATUSES = Set.of(OrderStatus.CREATED, OrderStatus.IN_PROCESSING);

    private static OrderService instance;
    private final OrderDB orderDB;

    private OrderService() {
        this.orderDB = OrderDB.getInstance();
    }

    public static OrderService getInstance() {
        if(instance == null) {
            instance = new OrderService();
        }

        return instance;
    }

    public Order save(Order order) {
        return orderDB.save(order);
    }

    public Order findByOrderNumber(int orderNumber) {
        return orderDB.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new NoSuchElementException("Заказ не найден!"));
    }

    public List<Order> findByStatus(OrderStatus orderStatus) {
        return orderDB.findByStatus(orderStatus);
    }


    public Order cancelOrder(int orderNumber) {
        Order order = findByOrderNumber(orderNumber);
        if (!PERMITTED_ORDER_STATUSES.contains(order.getStatus())) {
            throw new IllegalStateException("Невозможно отменить заказ!");
        }
        order.setStatus(OrderStatus.CANCELED);
        return save(order);
    }

    public void update(int orderNumber, OrderStatus orderStatus) {
        Order order = findByOrderNumber(orderNumber);

        order.setStatus(orderStatus);
    }

    public OrderDB getOrderDB() {
        return orderDB;
    }
}
