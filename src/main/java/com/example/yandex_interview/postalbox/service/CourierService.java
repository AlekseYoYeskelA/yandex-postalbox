package com.example.yandex_interview.postalbox.service;

import com.example.yandex_interview.postalbox.entity.Courier;
import com.example.yandex_interview.postalbox.enums.CourierStatus;
import com.example.yandex_interview.postalbox.enums.OrderStatus;
import com.example.yandex_interview.postalbox.exception.PostalBoxOverflowException;
import com.example.yandex_interview.postalbox.repo.CourierDB;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;

public final class CourierService {
    private static CourierService instance;
    private final CourierDB courierDB;
    private final PostalBoxService postalBoxService;
    private final OrderService orderService;
    private final int MAX_ORDERS_PER_COURIER = 3;

    private CourierService() {
        this.courierDB = CourierDB.getInstance();
        this.postalBoxService = PostalBoxService.getInstance();
        this.orderService = OrderService.getInstance();
    }

    public static CourierService getInstance() {
        if (instance == null) {
            instance = new CourierService();

        }
        return instance;
    }

    public Courier save(Courier courier) {
        return courierDB.save(courier);

        // Если объект содержится в списке,
        // то скорее всего мы его состояние уже обновили при поиске объекта и замене его полей
    }

    public Courier findCourierToDelivery() {
        return courierDB.findWithEmptyOrderNumberList()
                .orElseGet(() -> courierDB.findAll()
                        .stream()
                        .filter(courier -> courier.getOrderNumberList().size() <= MAX_ORDERS_PER_COURIER)
                        .min(Comparator.comparing(courier -> courier.getOrderNumberList().size()))
                        .orElseThrow(() -> new NoSuchElementException("Нет доступных курьеров")));
    }

    public void putOrder(Courier courier) {
        try {
            for (Integer orderNumber : new ArrayList<>(courier.getOrderNumberList())) {
                int freeCell = postalBoxService.putOrder(orderNumber);
                System.out.println("Заказ " + orderNumber + " положите в свободную ячейку " + freeCell);
                orderService.update(orderNumber, OrderStatus.DELIVERED);
                courier.removeOrderNumber(orderNumber);
            }
        } catch (PostalBoxOverflowException exception) {
            System.out.println(exception.getMessage() + "\nПопробуйте положить заказ на следующий день");
        }
    }
}
