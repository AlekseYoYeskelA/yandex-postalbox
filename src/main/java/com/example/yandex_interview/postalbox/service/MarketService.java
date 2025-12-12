package com.example.yandex_interview.postalbox.service;

import com.example.yandex_interview.postalbox.entity.Client;
import com.example.yandex_interview.postalbox.entity.Order;

//Market — это центр управления заказами.
// Маркет должен обращаться(работать) к списку клиентов. (аналог БД)
// отдельный класс под клиента, отдельный класс под список клиентов.
// Должен принимать номер заказа
// Должен помечать статус заказа (отдан клиенту) (прибыл)
// Добавить возможность отмены заказа (cancelOrder(numberOrder)) ?
//TODO Добавить логику по смене статуса.
//TODO Создать сервиc по сборке заказа.

//TODO все экземпляры сервисов и бд должны быть синглтон
public class MarketService {
    private static MarketService instance;
    private final ClientService clientService;
    private final OrderService orderService;

    private MarketService() {
        this.clientService = ClientService.getInstance();
        this.orderService = OrderService.getInstance();
    }

    public static MarketService getInstance() {
        if (instance == null) {
            instance = new MarketService();
        }
        return instance;
    }

    // TODO проверка существует ли клиент в бд?
    public String createOrder(Client client) {
        Order order = new Order();
        orderService.save(order);
        client.setOrder(order);
        clientService.save(client);
        return String.format("Ваш заказ принят. Номер заказа: %s", order.getOrderNumber());
    }

    public String cancelOrder(Order order) {
        int orderNumber = order.getOrderNumber();
        try {
            orderService.cancelOrder(orderNumber);
            return String.format("Ваш заказ №%s отменен", orderNumber);
        } catch (Exception e) {
            return String.format("Ваш заказ №%s не может быть отменен", orderNumber);
        }
    }
}
