package com.example.yandex_interview.postalbox.repo;

import com.example.yandex_interview.postalbox.entity.Client;

import java.util.*;

public final class ClientDB {
    private static ClientDB instance;
    private final List<Client> clientList;

    private ClientDB() {
        this.clientList = new ArrayList<>();
    }

    public static ClientDB getInstance() {
        if (instance == null) {
            instance = new ClientDB();
        }
        return instance;
    }

    public Client save(Client client) {
        clientList.add(client);
        return client;
    }

    public Client remove(Client client) {
        clientList.remove(client);
        return client;
    }

    public Optional<Client> findClientById(UUID id) {
        return clientList.stream()
                .filter(client -> id.equals(client.getId()))
                .findFirst();
    }

    public Optional<Client> findClientByOrderNumber(int orderNumber) {
        return clientList.stream()
                .filter(client -> client.getOrder().getOrderNumber() == orderNumber)
                .findFirst();
    }

    public List<Client> getClientList() {
        return clientList;
    }

    @Override
    public String toString() {
        return "ClientDB{" +
                "clientList=" + clientList +
                '}';
    }
}


