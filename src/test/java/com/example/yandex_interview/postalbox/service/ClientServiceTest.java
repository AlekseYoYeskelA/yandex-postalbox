package com.example.yandex_interview.postalbox.service;

import com.example.yandex_interview.postalbox.entity.Client;
import com.example.yandex_interview.postalbox.entity.Order;
import com.example.yandex_interview.postalbox.repo.ClientDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Disabled
@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientDB clientDB;

    @InjectMocks
    private ClientService clientService;

    private Client existingClient;
    private Client newClient;

    @BeforeEach
    void setUp() {
        existingClient = new Client("Aleksey",
                "Yeskela", "+12345678999");
        existingClient.setOrder(new Order());
        existingClient.setAccessCode(111111);

        newClient = new Client("New", "Client", "+99987654321");
        newClient.setOrder(new Order());
        newClient.setAccessCode(222222);
    }

    @Test
    @DisplayName("save: сохранение нового клиента, когда клиент не существует в базе")
    public void save_whenClientDoesNotExist_shouldSaveAndReturnClient() {
        when(clientDB.findClientById(newClient.getId())).thenReturn(Optional.empty());
        when(clientDB.save(newClient)).thenReturn(newClient);

        Client result = clientService.save(newClient);

        assertNotNull(result);
        assertSame(newClient, result);

        // Проверяем, что был вызван findClientById для проверки существования
        verify(clientDB).findClientById(newClient.getId());
        // Проверяем, что был вызван save для сохранения нового клиента
        verify(clientDB).save(newClient);
    }

    @Test
    @DisplayName("save: сохранение клиента, когда клиент уже существует в базе")
    public void save_whenClientAlreadyExists_shouldReturnExistingClientWithoutSaving() {
        UUID existingClientId = existingClient.getId();

        when(clientDB.findClientById(existingClientId))
                .thenReturn(Optional.of(existingClient));

        Client result = clientService.save(existingClient);

        assertNotNull(result);
        assertSame(existingClient, result); // Возвращается существующий клиент

        verify(clientDB).findClientById(existingClientId);
        verify(clientDB, never()).save(any(Client.class));
    }

    @Test
    @DisplayName("remove: удаление существующего клиента")
    public void remove_whenClientExist_shouldRemoveAndReturnRemovedClient() {
        UUID existingId= existingClient.getId();

        when(clientDB.findClientById(existingId))
                .thenReturn(Optional.of(existingClient));
        when(clientDB.remove(existingClient)).thenReturn(existingClient);

        Client result = clientService.remove(existingClient);

        assertSame(existingClient, result);
        verify(clientDB).findClientById(existingId);
        verify(clientDB).remove(existingClient);
    }

    @Test
    @DisplayName("remove: удаление несуществующего клиента - выбрасывается исключение")
    public void remove_whenClientNotExist_shouldThrownException() {
        UUID nonExistingId= existingClient.getId();

        when(clientDB.findClientById(nonExistingId))
                .thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            clientService.remove(existingClient);
        });

        assertEquals(String.format("Нет клиентов с id:%s ", nonExistingId), exception.getMessage());
        verify(clientDB).findClientById(nonExistingId);
        verify(clientDB, never()).remove(any(Client.class));
    }

    @Test
    @DisplayName("findClientByOrderNumber: поиск существующего клиента по номеру заказа")
    void findClientByOrderNumber_WhenClientExists_ShouldReturnClient() {
        int orderNumber = existingClient.getOrder().getOrderNumber();
        when(clientDB.findClientByOrderNumber(orderNumber))
                .thenReturn(Optional.of(existingClient));

        Client result = clientService.findClientByOrderNumber(orderNumber);

        assertNotNull(result);
        assertSame(existingClient, result);
        verify(clientDB).findClientByOrderNumber(orderNumber);
    }

    @Test
    @DisplayName("findClientByOrderNumber: поиск несуществующего клиента по номеру заказа - бросает исключение")
    void findClientByOrderNumber_WhenClientNotExists_ShouldThrowException() {
        int nonExistingOrderNumber = 999999;
        when(clientDB.findClientByOrderNumber(nonExistingOrderNumber))
                .thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            clientService.findClientByOrderNumber(nonExistingOrderNumber);
        });

        assertEquals("Нет клиентов с таким заказом", exception.getMessage());
        verify(clientDB).findClientByOrderNumber(nonExistingOrderNumber);
    }


    @Test
    @DisplayName("findClientById: поиск существующего клиента по id")
    void findClientById_WhenClientExists_ShouldReturnClient() {
        UUID clientId = existingClient.getId();
        when(clientDB.findClientById(clientId))
                .thenReturn(Optional.of(existingClient));

        Client result = clientService.findClientById(clientId);

        assertNotNull(result);
        assertSame(existingClient, result);
        verify(clientDB).findClientById(clientId);
    }

    @Test
    @DisplayName("findClientById: поиск несуществующего клиента по id - бросает исключение")
    void findClientById_WhenClientNotExists_ShouldThrowException() {
        UUID nonExistingId = UUID.randomUUID();
        when(clientDB.findClientById(nonExistingId))
                .thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            clientService.findClientById(nonExistingId);
        });

        assertTrue(exception.getMessage().contains("Нет клиентов с id:" + nonExistingId));
        verify(clientDB).findClientById(nonExistingId);
    }
}
