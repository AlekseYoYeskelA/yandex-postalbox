package com.example.yandex_interview.postalbox.repo;

import com.example.yandex_interview.postalbox.entity.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDBTest {
    private OrderDB orderDB;
    private Order order1;
    private Order order2;
    private Order order3;

    @BeforeEach
    void setUp() {
        orderDB = OrderDB.getInstance();

        order1 = new Order();
        order2 = new Order();
        order3 = new Order();
    }

    @AfterEach
    void clean() {
        orderDB.getOrderList().clear();
    }

    @Test
    @DisplayName("Сохранение нового заказа добавляет его в список")
    void save_NewOrder_ShouldAddOrder() {
        Order savedOrder = orderDB.save(order1);

        assertSame(order1, savedOrder);

        Optional<Order> foundOrder = orderDB.findByOrderNumber(order1.getOrderNumber());

        assertTrue(foundOrder.isPresent());
        assertSame(order1, foundOrder.get());
        assertEquals(1, orderDB.getOrderList().size());
    }

    @Test
    @DisplayName("Сохранение нескольких уникальных заказов")
    void save_MultipleUniqueOrders_ShouldAllBeSaved() {
        orderDB.save(order1);
        orderDB.save(order2);
        orderDB.save(order3);

        List<Order> orderList = orderDB.getOrderList();
        assertEquals(3, orderList.size());
        assertTrue(orderList.contains(order1));
        assertTrue(orderList.contains(order2));
        assertTrue(orderList.contains(order3));
    }

    @Test
    @DisplayName("Поиск заказа по существующему номеру")
    void findByOrderNumber_ExistingOrder_ShouldReturnOrder() {
        orderDB.save(order1);
        orderDB.save(order2);

        int orderNumber = order1.getOrderNumber();

        Optional<Order> foundOrder = orderDB.findByOrderNumber(orderNumber);

        assertTrue(foundOrder.isPresent());
        assertSame(order1, foundOrder.get());
        assertEquals(orderNumber, foundOrder.get().getOrderNumber());
    }

    @Test
    @DisplayName("Поиск заказа по несуществующему номеру")
    void findByOrderNumber_NonExistingOrder_ShouldReturnEmptyOptional() {
        Optional<Order> foundOrder = orderDB.findByOrderNumber(999999);

        assertFalse(foundOrder.isPresent());
    }
}
