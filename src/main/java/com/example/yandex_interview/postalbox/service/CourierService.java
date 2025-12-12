package com.example.yandex_interview.postalbox.service;

import com.example.yandex_interview.postalbox.entity.Courier;
import com.example.yandex_interview.postalbox.enums.CourierStatus;
import com.example.yandex_interview.postalbox.repo.CourierDB;

import java.util.Comparator;
import java.util.NoSuchElementException;

public final class CourierService {
    private static CourierService instance;
    private final CourierDB courierDB;

    private CourierService() {
        this.courierDB = CourierDB.getInstance();
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

    //todo переименовать метод более точно?
    public Courier findCourierToDelivery() {
        return courierDB.findWithEmptyOrderNumberList()
                .orElseGet(() -> courierDB.findAll()
                        .stream()
                        .filter(courier -> courier.getOrderNumberList().size() <= 3)
                        .min(Comparator.comparing(courier -> courier.getOrderNumberList().size()))
                        .orElseThrow(() -> new NoSuchElementException("Нет доступных курьеров")));
    }
}
