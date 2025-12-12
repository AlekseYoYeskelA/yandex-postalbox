package com.example.yandex_interview.postalbox.service;

import com.example.yandex_interview.postalbox.entity.Courier;
import com.example.yandex_interview.postalbox.entity.Order;
import com.example.yandex_interview.postalbox.enums.CourierStatus;
import com.example.yandex_interview.postalbox.enums.OrderStatus;

import java.util.List;

//TODO 22.11.2025 продумать логику паттерна синглтон
//TODO 22.11.2025 курьер будет отдельной сущностью
//TODO добавить интерфейс и вынести его реализацию
public class OrderProcessService {

    private final OrderService orderService;
    private final CourierService courierService;

    public OrderProcessService() {
        this.orderService = OrderService.getInstance();
        this.courierService = CourierService.getInstance();
    }

    public int getOrderToDelivery(int orderNumber) {
        // TODO в методе мы должны сменить статус заказа на следующий IN_DELIVERY
        // TODO метод возвращает номер заказа
        return 1;
    }

    public void orderProcess() {
        orderService.findByStatus(OrderStatus.CREATED)
                .forEach(order -> orderService.update(order.getOrderNumber(), OrderStatus.IN_PROCESSING));
        List<Order> orderList = orderService.findByStatus(OrderStatus.IN_PROCESSING);

        //назначить курьера, используя findCourierForDelivery(готово)


        // так как свободных курьеров может быть меньше чем заказов
        for (Order order : orderList) {
            // todo 06.12.2025 свободных курьеров может быть меньше, чем заказов для доставки,
            //  придется скорее всего сделать блоки try catch для каждой итерации и обрабатывать ошибку,
            int currentOrderNumber = order.getOrderNumber();
            // пока не используется
            Courier freeCourier = findCourierForDelivery(currentOrderNumber);
            orderService.update(currentOrderNumber, OrderStatus.IN_DELIVERY);
        }
    }

    //добавить список заказов определенному курьеру и сохранить в бд  (готово)
    private Courier findCourierForDelivery(int orderNumber) {
        Courier courierForDelivery = courierService.findCourierToDelivery();
        // todo 06.12.2025 возможно нужно переместить логику добавления заказа в список заказов курьера в метод orderProcess() или вынести в отдельный метод
        courierForDelivery.getOrderNumberList().add(orderNumber);
        return courierService.save(courierForDelivery);
    }
}

class TestOrderProcessService {
    public static void main(String[] args) {
        OrderProcessService orderProcessService = new OrderProcessService();
        orderProcessService.orderProcess();

    }
}
