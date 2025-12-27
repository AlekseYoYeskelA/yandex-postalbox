package com.example.yandex_interview.postalbox.service;

import com.example.yandex_interview.postalbox.entity.Client;
import com.example.yandex_interview.postalbox.entity.Order;
import com.example.yandex_interview.postalbox.repo.ClientDB;

import java.util.NoSuchElementException;
import java.util.UUID;

public final class ClientService {
    private static ClientService instance;
    private final ClientDB clientDB;
    private final PostalBoxService postalBoxService;

    private ClientService() {
        this.clientDB = ClientDB.getInstance();
        this.postalBoxService = PostalBoxService.getInstance();
    }

    public static ClientService getInstance() {
        if (instance == null) {
            instance = new ClientService();
        }
        return instance;
    }
    // 1

    //todo orElseGet() обеспечивает ленивый вызов функции в параметре
    public Client save(Client client) {
        return clientDB.findClientById(client.getId())
                .orElseGet(() -> clientDB.save(client));
    }

    //Неатомарное выполнение, в Spring вешаем @Transactional
    public Client remove(Client client) {
        findClientById(client.getId());
        return clientDB.remove(client);
    }

    // Один запрос для проверки и удаления
    public Client atomicRemove(Client client) {
        return clientDB.findClientById(client.getId())
                .map(clientDB::remove)
                .orElseThrow(() -> new NoSuchElementException("Нет клиентов с id:" + client.getId()));
    }

    public Client findClientByOrderNumber(int orderNumber) {
        return clientDB.findClientByOrderNumber(orderNumber)
                .orElseThrow(() -> new NoSuchElementException("Нет клиентов с таким заказом"));
    }

    public Client findClientById(UUID id) {
        return clientDB.findClientById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("Нет клиентов с id:%s ", id)));
    }

    public ClientDB getClientDB() {
        return clientDB;
    }

    public void pickOrder(int SMSCode){
        postalBoxService.pickOrder(SMSCode);
    }
}
