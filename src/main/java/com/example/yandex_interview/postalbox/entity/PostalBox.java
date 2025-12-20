package com.example.yandex_interview.postalbox.entity;

import com.example.yandex_interview.postalbox.UserNotificationApi;
import com.example.yandex_interview.postalbox.UserNotificationApiImpl;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class PostalBox {
    //Кey - номер ячейки, Value - номер заказа
    private final Map<Integer, Integer> orderMap;

    //Key - код, Value - номер ячейки
    private final Map<Integer, Integer> codeMap;

    //Свободные ячейки
    private final Deque<Integer> freeCells;

    //Емкость постамата (кол-во ячеек)
    private final int capacity;

    public PostalBox(int capacity) {
        this.orderMap = new HashMap<>();
        this.codeMap = new HashMap<>();
        this.freeCells = new ArrayDeque<>();
        this.capacity = capacity;

        for (int i = 1; i <= capacity; i++) {
            freeCells.offer(i);
        }
    }

    public Map<Integer, Integer> getOrderMap() {
        return orderMap;
    }

    public Map<Integer, Integer> getCodeMap() {
        return codeMap;
    }

    public Deque<Integer> getFreeCells() {
        return freeCells;
    }

    public int getCapacity() {
        return capacity;
    }
}
