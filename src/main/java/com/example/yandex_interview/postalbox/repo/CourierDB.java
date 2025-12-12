package com.example.yandex_interview.postalbox.repo;

import com.example.yandex_interview.postalbox.entity.Courier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class CourierDB {
    private static CourierDB instance;
    private final List<Courier> courierList;

    private CourierDB() {
        this.courierList = new ArrayList<>();
    }

    public static CourierDB getInstance() {
        if (instance == null) {
            instance = new CourierDB();
        }
        return instance;
    }

    public List<Courier> findAll() {
        return courierList;
    }

    public Optional<Courier> findWithEmptyOrderNumberList() {
        return courierList.stream()
                .filter(Courier::isNotBusy)
                .findFirst();
    }

    public Courier save(Courier courier) {

        if (!courierList.contains(courier)) {
            courierList.add(courier);
        }
        return courier;

        // Если объект содержится в списке, то скорее всего мы его состояние уже обновили
        // при поиске объекта и замене его полей
    }

    //todo реализуем позже при необходимости
    public void update() {

    }
}
