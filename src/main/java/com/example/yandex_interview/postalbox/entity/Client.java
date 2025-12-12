package com.example.yandex_interview.postalbox.entity;

import java.util.UUID;

// Идентификатор клиента.
public class Client {
    private final String lastName;
    private final String firstName;
    private final UUID id;
    private Order order;
    private final String phone;
    private int accessСode;


    public Client(String lastName, String firstName, String phone) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.phone = phone;
        this.id = UUID.randomUUID();
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setAccessСode(int accessСode) {
        this.accessСode = accessСode;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public UUID getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public int getAccessСode() {
        return accessСode;
    }

    @Override
    public String toString() {
        return "Client{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", id=" + id +
                ", order=" + order +
                ", phone='" + phone + '\'' +
                ", accessСode=" + accessСode +
                '}';
    }
}
