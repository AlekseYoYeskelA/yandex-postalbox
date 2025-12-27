package com.example.yandex_interview.postalbox.entity;

import com.example.yandex_interview.postalbox.enums.OrderStatus;

import java.util.Objects;

//TODO Подумать о логике обработке ошибки при создании заказа.
public class Order {
    private final int orderNumber;
    private OrderStatus orderStatus;

    public Order() {
        this.orderNumber = OrderNumberGenerator.generate();
        this.orderStatus = OrderStatus.CREATED;

    }

    //конструктор для тестов
    public Order(int orderNumber) {
        this.orderNumber = orderNumber;
        this.orderStatus = OrderStatus.CREATED;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNumber=" + orderNumber +
                ", status=" + orderStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderNumber == order.orderNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber);
    }
}

class OrderNumberGenerator {
    private static int lastNumber = 1;

    public static int generate() {
        return lastNumber++;
    }
}
