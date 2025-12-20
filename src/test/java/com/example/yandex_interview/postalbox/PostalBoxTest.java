package com.example.yandex_interview.postalbox;

import com.example.yandex_interview.postalbox.exception.InvalidAccessCodeException;
import com.example.yandex_interview.postalbox.exception.PostalBoxOverflowException;
import com.example.yandex_interview.postalbox.service.PostalBoxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PostalBoxTest {
    private PostalBoxService postalBox;

    @Mock
    private UserNotificationApi mockUserNotificationApi;

//
//    @BeforeEach
//    void setUp() {
//        postalBox = new PostalBoxService(3, mockUserNotificationApi);
//    }

    @Test
    @DisplayName("Тест putOrder() при наличии свободных ячеек")
    public void putOrder_whenFreeCellAvailable_shouldReturnCellNumber() {
        int orderNumber = 1234;
        int expectedCode = 999999;
        int expectedCell = 1;

        when(mockUserNotificationApi.sendNotificationWithAccessCode(orderNumber))
                .thenReturn(expectedCode);

        int actualCell = postalBox.putOrder(orderNumber);

        assertEquals(expectedCell, actualCell);
        verify(mockUserNotificationApi).sendNotificationWithAccessCode(orderNumber);

    }

    @Test
    @DisplayName("Тест putOrder() при отсутствии свободных ячеек")
    public void putOrder_whenFreeCellNotAvailable_shouldThrownException() {
        int orderNumber = 1234;

        for (int i = 0; i < postalBox.getCapacity(); i++) {
            when(mockUserNotificationApi.sendNotificationWithAccessCode(anyInt()))
                    .thenReturn(100000 + i);
            postalBox.putOrder(orderNumber + i);
        }

        assertThrows(PostalBoxOverflowException.class, () -> postalBox.putOrder(orderNumber));
    }


    @Test
    @DisplayName("Тест pickOrder() при вводе верного кода")
    public void pickOrder_whenEnterValidCode_shouldCompleteSuccessfully() {
        int orderNumber = 1234;
        int expectedCode = 999999;

        when(mockUserNotificationApi.sendNotificationWithAccessCode(orderNumber))
                .thenReturn(expectedCode);

        postalBox.putOrder(orderNumber);

        assertDoesNotThrow(() -> postalBox.pickOrder(expectedCode));
        verify(mockUserNotificationApi).sendSuccessNotification(expectedCode, orderNumber);
    }

    @Test
    @DisplayName("Тест pickOrder() при вводе неверного кода")
    public void pickOrder_whenEnterNotValidCode_shouldThrownException() {
        int invalidCode = 000000;

        assertThrows(InvalidAccessCodeException.class, () -> postalBox.pickOrder(invalidCode));
        verify(mockUserNotificationApi, never()).sendSuccessNotification(anyInt(), anyInt());

    }
}