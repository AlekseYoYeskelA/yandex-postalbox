package com.example.yandex_interview.postalbox.repo;

import com.example.yandex_interview.postalbox.entity.Client;
import com.example.yandex_interview.postalbox.entity.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class ClientDBTest {
    private ClientDB clientDB;
    private Client client1;
    private Client client2;

    @BeforeEach
    void setUp() {
        clientDB = ClientDB.getInstance();

        client1 = new Client("Petrov", "Peter", "+79998885533");
        client2 = new Client("Ivanov", "Ivan", "+79997776622");

        Order order1 = new Order();
        Order order2 = new Order();

        client1.setOrder(order1);
        client2.setOrder(order2);

        client1.setAccessСode(111111);
        client2.setAccessСode(222222);
    }

    @AfterEach
    void clean() {
        clientDB.getClientList().clear();
    }

    @Test
    @DisplayName("Сохранение клиента добавляет его в базу данных")
    public void save_shouldAddClientToDB() {
        Client savedClient = clientDB.save(client1);

        assertSame(client1, savedClient);

        Optional<Client> foundClient = clientDB.findClientById(client1.getId());

        assertTrue(foundClient.isPresent());
        assertSame(client1, foundClient.get());

        List<Client> clientList = clientDB.getClientList();
        assertEquals(1, clientList.size());
        assertTrue(clientList.contains(client1));
    }

    @Test
    @DisplayName("Сохранение нескольких клиентов работает корректно")
    void save_multipleClients_shouldAllBeSaved() {
        clientDB.save(client1);
        clientDB.save(client2);

        List<Client> clientList = clientDB.getClientList();
        assertEquals(2, clientList.size());
        assertTrue(clientList.contains(client1));
        assertTrue(clientList.contains(client2));
    }

    @Test
    @DisplayName("Удаление существующего клиента")
    void remove_ExistingClient_ShouldRemoveFromList() {
        clientDB.save(client1);
        clientDB.save(client2);

        Client removedClient = clientDB.remove(client1);

        assertSame(client1, removedClient);

        List<Client> clientList = clientDB.getClientList();
        assertEquals(1, clientList.size());
        assertFalse(clientList.contains(client1));
        assertTrue(clientList.contains(client2));
    }

    @Test
    @DisplayName("Удаление несуществующего клиента не вызывает исключения")
    void remove_NonExistingClient_ShouldNotThrowException() {
        assertDoesNotThrow(() -> {
            Client removedClient = clientDB.remove(client1);
            assertSame(client1, removedClient);
        });

        // Проверяем, что список остался пустым
        List<Client> clientList = clientDB.getClientList();
        assertTrue(clientList.isEmpty());
    }

    @Test
    @DisplayName("Поиск клиента по существующему ID")
    void findClientById_ExistingClient_ShouldReturnClient() {
        clientDB.save(client1);

        Optional<Client> foundClient = clientDB.findClientById(client1.getId());

        assertTrue(foundClient.isPresent());
        assertSame(client1, foundClient.get());
        assertEquals("Petrov", foundClient.get().getLastName());
        assertEquals("Peter", foundClient.get().getFirstName());
        assertEquals("+79998885533", foundClient.get().getPhone());

        // Дополнительная проверка через getClientList()
        assertEquals(1, clientDB.getClientList().size());
    }

    @Test
    @DisplayName("Поиск клиента по несуществующему ID")
    void findClientById_NonExistingClient_ShouldReturnEmptyOptional() {
        Optional<Client> foundClient = clientDB.findClientById(UUID.randomUUID());

        assertFalse(foundClient.isPresent());

        // Проверяем, что список пуст
        assertTrue(clientDB.getClientList().isEmpty());
    }
}
