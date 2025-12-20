package com.example.yandex_interview.postalbox.service;

import com.example.yandex_interview.postalbox.UserNotificationApi;
import com.example.yandex_interview.postalbox.UserNotificationApiImpl;
import com.example.yandex_interview.postalbox.entity.PostalBox;
import com.example.yandex_interview.postalbox.exception.InvalidAccessCodeException;
import com.example.yandex_interview.postalbox.exception.PostalBoxOverflowException;

import java.util.*;

/**
 * Постамат - автоматическая станция приёма/выдачи посылок.
 * В маркете формируются заказы, и хочется добавить возможность получения через постамат.
 * Запускаем MVP: небольшая аудитория пользователей, несколько постаматов в Москве.
 * При заказе пользователь сможет выбрать, что хочет получить заказ в постамате.
 * <p>
 * В рамках задачи нужно реализовать код для MVP решения:
 * - курьер привозит заказ и пробует положить его в ячейку, указывая номер заказа. Постамат сам выбирает ячейку и возвращает в ответ.
 * Она откроется вызывающим этот метод кодом.
 * - после того, как заказ положили в ячейку, пользователю отправляется СМС c кодом получения. Заказ будет ждать вечно
 * - в случае любых ошибок - курьер забирает заказ назад и попробует положить заказ в ячейку на следующий день (для MVP это ок)
 * - пользователь может получить заказ по коду выдачи из СМС. При вводе кода выдачи постамат должен вывести на экран текст
 * "ваш заказ ХХХ в ячейке YYY", ячейка откроется сама.
 * <p>
 * Ограничения:
 * - все ячейки одного размера, но их может быть разное количество, зависит от конкретного постамата
 * - один заказ - одна коробка, она влезает в ячейку
 * - ячейки каждого постамата пронумерованы
 * - каждый постамат сам хранит своё состояние
 * <p>
 * Для отправки сообщения пользователю надо использовать клиент UserNotificationApi.
 * <p>
 * Масштабирование:
 * Создать класс маркета, который и умеет обращаться к списку клиентов/бд (ClientBase), генерировать номер заказа,
 * устанавливать связь номер заказа - клиент, номер заказа - курьер
 * Создать класс клиента (Client), который будет забирать полсылку (). Отправляем нотификацию конкретному клиенту по номеру заказа
 * Клиент будет посылать запрос на создание заказа
 */

public class PostalBoxService {
    private final UserNotificationApi notificationApi;
    private final PostalBox postalBox;
    private static PostalBoxService instance;

    private PostalBoxService() {
        this.notificationApi = UserNotificationApiImpl.getInstance();
        postalBox = new PostalBox(5);
    }

    public static PostalBoxService getInstance() {
        if (instance == null) {
            instance = new PostalBoxService();
        }
        return instance;
    }

// нужно реализовать методы хранения и выдачи заказа

    /**
     * принимает номер заказа,
     * ищет свободную ячейку
     * если свободная ячейка найдена, помечает ее как занятую, генерирует код выдачи (например, случайный строковый код)
     * отправляет СМС с кодом выдачи через UserNotificationApi
     * возвращает номер ячейки, в которую помещен заказ
     * (или код ошибки, но по условию при ошибке курьер забирает заказ назад, поэтому в случае ошибки бросим исключение)
     *
     * @param order - номер заказа
     */
    public int putOrder(int order) {
        Deque<Integer> freeCells = postalBox.getFreeCells();

        if (freeCells.isEmpty()) {
            throw new PostalBoxOverflowException("Нет свободных ячеек");
        }

        int freeCell = freeCells.pollFirst();
        postalBox.getOrderMap().put(freeCell, order);

        //отправляем код пользователю
        int code = notificationApi.sendNotificationWithAccessCode(order);

        //Кладем код в мапу
        postalBox.getCodeMap().put(code, freeCell);
        return freeCell;
    }

    /**
     * Принимает код выдачи.
     * Ищет ячейку, связанную с этим кодом.
     * Если находит, то освобождает ячейку и возвращает сообщение с номером заказа и ячейки,
     * а также открывает ячейку (в нашем случае, просто выводим сообщение, что ячейка открыта).
     * Отправляет СМС с уведомлением о получении заказа через UserNotificationApi
     *
     * @param code - код выдачи
     */
    public void pickOrder(int code) {
        Map<Integer, Integer> codeMap = postalBox.getCodeMap();
        if (!codeMap.containsKey(code)) {
            throw new InvalidAccessCodeException("Код введен неверно, попробуйте снова");
        }

        int cellForOpen = codeMap.get(code);
        codeMap.remove(code);
        // удаляем заказ из ячейки
        int orderNumber = postalBox.getOrderMap().remove(cellForOpen);
        postalBox.getFreeCells().offer(cellForOpen);
        System.out.printf("Ваш заказ №%s в ячейке №%s%n", orderNumber, cellForOpen);
        notificationApi.sendSuccessNotification(code, orderNumber);
    }

    public UserNotificationApi getNotificationApi() {
        return notificationApi;
    }

    public int getCapacity() {
        return postalBox.getCapacity();
    }


    //для отладки
//    public static void main(String[] args) {
//        PostalBoxService postalBox = new PostalBoxService(3, UserNotificationApiImpl.getInstance());
//        UserNotificationApiImpl api = (UserNotificationApiImpl) postalBox.getNotificationApi();
//        System.out.println("Активные коды доступа:" + api.getCodeManager().getActiveCodes());
//
//        postalBox.putOrder(7981);
//        postalBox.putOrder(6156);
//        postalBox.putOrder(3456);
//
//        System.out.println("Активные коды доступа:" + api.getCodeManager().getActiveCodes());
//
//
//        System.out.printf("Мапа '№ ячейки' = '№ заказа' (orderMap): %s%n", postalBox.orderMap);
//        System.out.printf("Мапа 'код' = '№ ячейки' (codeMap): %s%n", postalBox.codeMap);
//        System.out.printf("Список свободных ячеек' (freeCells): %s%n", postalBox.freeCells);
//
//        //костыль для поиска кода
//        var code = postalBox.codeMap.keySet().stream()
//                .findAny()
//                .orElseThrow(() -> new NoSuchElementException("Нет кода"));
//
//        postalBox.pickOrder(code);
//
//        var code2 = postalBox.codeMap.keySet().stream()
//                .findAny()
//                .orElseThrow(() -> new NoSuchElementException("Нет кода"));
//
//        postalBox.pickOrder(code2);
//
//        var code3 = postalBox.codeMap.keySet().stream()
//                .findAny()
//                .orElseThrow(() -> new NoSuchElementException("Нет кода"));
//
//        postalBox.pickOrder(code3);
//
//
//        System.out.printf("Список свободных ячеек (freeCells): %s%n", postalBox.freeCells);
//        System.out.println("Активные коды доступа:" + api.getCodeManager().getActiveCodes());
//
//    }
}







