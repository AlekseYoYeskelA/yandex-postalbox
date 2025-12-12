package com.example.yandex_interview.postalbox;

public class UserNotificationApiImpl implements UserNotificationApi {
    private final CodeManager codeManager;

    public UserNotificationApiImpl() {
        this.codeManager = new CodeManager();
    }

    @Override
    public int sendNotificationWithAccessCode(int orderNumber) {
        int code = codeManager.generate();
        System.out.printf("Ваш заказ №%s готов к получению. Код получения: %s%n", orderNumber, code);
        return code;
    }

    @Override
    public void sendSuccessNotification(int code, int orderNumber) {
        codeManager.setInactive(code);
        System.out.printf("Заказ №%s успешно получен. Ждём Вас снова!%n", orderNumber);
    }

    public CodeManager getCodeManager() {
        return codeManager;
    }
}
