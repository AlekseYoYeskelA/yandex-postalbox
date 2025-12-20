package com.example.yandex_interview.postalbox.service;

import com.example.yandex_interview.postalbox.entity.Courier;
import com.example.yandex_interview.postalbox.entity.Order;
import com.example.yandex_interview.postalbox.enums.OrderStatus;

import java.util.List;
import java.util.NoSuchElementException;

//TODO добавить интерфейс и вынести его реализацию
public class OrderProcessService {
    private final OrderService orderService;
    private final CourierService courierService;

    public OrderProcessService() {
        this.orderService = OrderService.getInstance();
        this.courierService = CourierService.getInstance();
    }

    public void orderProcess() {
        orderService.findByStatus(OrderStatus.CREATED)
                .forEach(order -> orderService.update(order.getOrderNumber(), OrderStatus.IN_PROCESSING));
        List<Order> orderList = orderService.findByStatus(OrderStatus.IN_PROCESSING);

        try {
            for (Order order : orderList) {
                int currentOrderNumber = order.getOrderNumber();
                assignOrderNumberToCourier(currentOrderNumber);
                orderService.update(currentOrderNumber, OrderStatus.IN_DELIVERY);
            }
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage() + ". Попробуйте запросить позже.");
        }
    }



    private void assignOrderNumberToCourier(int orderNumber) {
        Courier courierForDelivery = courierService.findCourierToDelivery();
        courierForDelivery.getOrderNumberList().add(orderNumber);
        courierService.save(courierForDelivery);
    }
}

class TestOrderProcessService {
    public static void main(String[] args) {
        OrderProcessService orderProcessService = new OrderProcessService();
        orderProcessService.orderProcess();
    }
}
