package com.example.yandex_interview.postalbox.entity;

import java.util.ArrayList;
import java.util.List;

public class Courier {
    private final Long id;
    private final String fullName;
    private final List<Integer> orderNumberList;

    public Courier(String fullName) {
        this.id = CourierIdGenerator.generate();
        this.fullName = fullName;
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

    public void removeOrderNumber(int orderNumber){
        orderNumberList.remove((Integer) orderNumber);
    }
}

class CourierIdGenerator {
    private static long lastId = 1;

    public static long generate() {
        return lastId++;
    }
}