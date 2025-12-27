package com.example.yandex_interview.postalbox.service;

import com.example.yandex_interview.postalbox.entity.Order;
import com.example.yandex_interview.postalbox.enums.OrderStatus;
import com.example.yandex_interview.postalbox.repo.OrderDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Disabled
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderDB orderDB;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private int orderNumber;

    @BeforeEach
    void setUp() {
        order = new Order();
        orderNumber = order.getOrderNumber();
    }

    @Test
    @DisplayName("savе: сохранение нового заказа")
    public void save_shouldSaveAndReturnOrder() {
        when(orderDB.save(order)).thenReturn(order);

        Order result = orderService.save(order);

        assertNotNull(result);
        assertSame(order, result);
        verify(orderDB).save(order);
    }

    @Test
    @DisplayName("save: повторное сохранение того же заказа")
    void save_SameOrderTwice_ShouldHandleCorrectly() {
        when(orderDB.save(order)).thenReturn(order);

        Order result1 = orderService.save(order);
        Order result2 = orderService.save(order);

        assertSame(order, result1);
        assertSame(order, result2);
        verify(orderDB, times(2)).save(order);
    }


    @Test
    @DisplayName("findByOrderNumber: поиск существующего заказа по номеру")
    void findByOrderNumber_whenOrderExists_shouldReturnOrder() {
        when(orderDB.findByOrderNumber(orderNumber))
                .thenReturn(Optional.of(order));

        Order result = orderService.findByOrderNumber(orderNumber);

        assertNotNull(result);
        assertSame(order, result);
        verify(orderDB).findByOrderNumber(orderNumber);
    }

    @Test
    @DisplayName("findByOrderNumber: поиск несуществующего заказа по номеру")
    void findByOrderNumber_WhenOrderNotExists_ShouldThrowException() {
        int nonExistingOrderNumber = 999999;
        when(orderDB.findByOrderNumber(nonExistingOrderNumber))
                .thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            orderService.findByOrderNumber(nonExistingOrderNumber);
        });

        assertEquals("Заказ не найден!", exception.getMessage());
        verify(orderDB).findByOrderNumber(nonExistingOrderNumber);
    }

    @Test
    @DisplayName("cancelOrder: заказ в разрешенном статусе - отменяет успешно")
    void cancelOrder_WhenPermittedStatus_ShouldCancelOrder() {
        order.setStatus(OrderStatus.CREATED); // Разрешенный статус
        when(orderDB.findByOrderNumber(orderNumber))
                .thenReturn(Optional.of(order));
        when(orderDB.save(order)).thenReturn(order);

        Order result = orderService.cancelOrder(orderNumber);

        assertNotNull(result);
        assertEquals(OrderStatus.CANCELED, result.getOrderStatus());
        verify(orderDB).findByOrderNumber(orderNumber);
        verify(orderDB).save(order);
    }

    @Test
    @DisplayName("cancelOrder: заказ в запрещенном статусе - бросает исключение")
    void cancelOrder_WhenNotPermittedStatus_ShouldThrowException() {
        order.setStatus(OrderStatus.DELIVERED); // Запрещенный статус
        when(orderDB.findByOrderNumber(orderNumber))
                .thenReturn(Optional.of(order));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            orderService.cancelOrder(orderNumber);
        });

        assertEquals("Невозможно отменить заказ!", exception.getMessage());
        verify(orderDB).findByOrderNumber(orderNumber);
        verify(orderDB, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("update: заказ найден - обновляет статус")
    void update_WhenOrderExists_ShouldUpdateStatus() {
        OrderStatus newOrderStatus = OrderStatus.DELIVERED;
        when(orderDB.findByOrderNumber(orderNumber))
                .thenReturn(Optional.of(order));
        when(orderDB.save(order)).thenReturn(order);

        orderService.update(orderNumber, newOrderStatus);

//        assertNotNull(result);
//        assertEquals(newOrderStatus, result.getStatus());
        verify(orderDB).findByOrderNumber(orderNumber);
        verify(orderDB).save(order);
    }

    @Test
    @DisplayName("update: заказ не найден - бросает исключение")
    void update_WhenOrderNotExists_ShouldThrowException() {
        int nonExistingOrderNumber = 999999;
        OrderStatus newOrderStatus = OrderStatus.DELIVERED;
        when(orderDB.findByOrderNumber(nonExistingOrderNumber))
                .thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            orderService.update(nonExistingOrderNumber, newOrderStatus);
        });

        assertEquals("Заказ не найден!", exception.getMessage());
        verify(orderDB).findByOrderNumber(nonExistingOrderNumber);
        verify(orderDB, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("cancelOrder: проверка всех разрешенных статусов")
    void cancelOrder_AllPermittedStatuses_ShouldCancelSuccessfully() {
        // Проверяем все статусы из PERMITTED_STATUSES
        OrderStatus[] permittedOrderStatuses = {OrderStatus.CREATED, OrderStatus.IN_PROCESSING};

        for (OrderStatus orderStatus : permittedOrderStatuses) {
            Order testOrder = new Order();
            testOrder.setStatus(orderStatus);
            int testOrderNumber = testOrder.getOrderNumber();

            when(orderDB.findByOrderNumber(testOrderNumber))
                    .thenReturn(Optional.of(testOrder));
            when(orderDB.save(testOrder)).thenReturn(testOrder);

            Order result = orderService.cancelOrder(testOrderNumber);

            assertEquals(OrderStatus.CANCELED, result.getOrderStatus());

            reset(orderDB);
        }
    }

    @Test
    @DisplayName("cancelOrder: проверка запрещенных статусов")
    void cancelOrder_NotPermittedStatus_ShouldThrowForEach() {
        // Берем статусы, которые точно не входят в PERMITTED_STATUSES
        // (предполагая, что есть другие статусы кроме CREATED, IN_PROGRESS, CANCELED)
        OrderStatus[] notPermittedOrderStatuses = {OrderStatus.DELIVERED, OrderStatus.CANCELED};

        for (OrderStatus orderStatus : notPermittedOrderStatuses) {
            Order testOrder = new Order();
            testOrder.setStatus(orderStatus);
            int testOrderNumber = testOrder.getOrderNumber();

            when(orderDB.findByOrderNumber(testOrderNumber))
                    .thenReturn(Optional.of(testOrder));

            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
                orderService.cancelOrder(testOrderNumber);
            });

            assertEquals("Невозможно отменить заказ!", exception.getMessage());

            reset(orderDB);
        }
    }
}
