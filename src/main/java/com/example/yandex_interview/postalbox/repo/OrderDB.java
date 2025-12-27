package com.example.yandex_interview.postalbox.repo;

import com.example.yandex_interview.postalbox.entity.Order;
import com.example.yandex_interview.postalbox.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class OrderDB {
    private static OrderDB instance;
    private final List<Order> orderList;

    private OrderDB() {
        this.orderList = new ArrayList<>();
    }

    public static OrderDB getInstance() {
        if (instance == null) {
            instance = new OrderDB();
        }
        return instance;
    }

    public Order save(Order order) {
        if (!orderList.contains(order)) {
            orderList.add(order);
        }
        return order;
    }

    public Optional<Order> findByOrderNumber(int orderNumber) {
        return orderList.stream()
                .filter(order -> orderNumber == order.getOrderNumber())
                .findFirst();
        //.orElseThrow(() -> new NoSuchElementException("Заказ не найден!"));
    }

    public List<Order> findByStatus(OrderStatus orderStatus) {
        return orderList.stream()
                .filter(order -> order.getOrderStatus() == orderStatus)
                .toList();
    }

    public List<Order> getOrderList() {
        return orderList;
    }
}
