package com.example.yandex_interview.postalbox.entity;

import com.example.yandex_interview.postalbox.enums.CourierStatus;

import java.util.ArrayList;
import java.util.List;

public class Courier {
    private final Long id;
    private final String fullName;
    private CourierStatus courierStatus;
    private final List<Integer> orderNumberList;

    public Courier(String fullName) {
        this.id = CourierIdGenerator.generate();
        this.fullName = fullName;
        this.courierStatus = CourierStatus.NOT_BUSY;
        this.orderNumberList = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public List<Integer> getOrderNumberList() {
        return orderNumberList;
    }

    public boolean isNotBusy() {
        return orderNumberList.isEmpty();
    }

    public CourierStatus getCourierStatus() {
        return courierStatus;
    }

    public void setCourierStatus(CourierStatus courierStatus) {
        this.courierStatus = courierStatus;
    }
}

class CourierIdGenerator {
    private static long lastId = 1;

    public static long generate() {
        return lastId++;
    }
}