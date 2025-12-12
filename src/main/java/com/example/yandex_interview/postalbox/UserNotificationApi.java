package com.example.yandex_interview.postalbox;

/**
 * Синхронный клиент, вызывающий postalbox.notify.market.yandex.net
 * Реализацию интерфейса описывать не нужно.
 */
public interface UserNotificationApi {
    // нужно описать метод(ы) для отправки сообщения с кодом выдачи
    // в ответ придёт код выдачи, который был отправлен пользователю
    int sendNotificationWithAccessCode(int orderNumber);
    void sendSuccessNotification(int code, int orderNumber);
}
