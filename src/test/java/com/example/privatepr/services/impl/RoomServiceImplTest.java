package com.example.privatepr.services.impl;

import com.example.privatepr.models.Hotel;
import com.example.privatepr.models.Room;
import com.example.privatepr.repositories.RoomRepository;
import com.example.privatepr.utils.erorsHandler.ErrorHandler;
import com.example.privatepr.utils.exeptions.RoomErrorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ErrorHandler errorHandler;
    @InjectMocks
    private RoomServiceImpl roomService;


    @Test
    void delete_roomNotFound_shouldThrowException() {
        int roomId = 22;

        when(roomService.getRoom(roomId)).thenReturn(Optional.empty());
        when(errorHandler.getErrorMessage(anyString())).thenReturn("Error message");

        assertThrows(RoomErrorException.class, () -> roomService.delete(roomId));

        // deleteById не должен быть вызван
        verify(roomRepository, never()).deleteById(anyInt());
    }


    @Test
    void update_roomExist_shouldUpdateRoom() {
        Room roomInDB = getRoom();
        int roomId = 1;
        roomInDB.setId(roomId);
        Hotel hotelInDB = new Hotel();
        roomInDB.setHotel(hotelInDB);

        Room roomByUpdate = new Room();

        when(roomRepository.findById(roomId)).thenReturn(Optional.of(roomInDB));
        when(roomRepository.save(roomByUpdate)).thenReturn(roomByUpdate);

        roomService.update(roomId, roomByUpdate);

        // findById вызывается ровно один раз с правильным id
        verify(roomRepository, times(1)).findById(roomId);

        // save вызывается ровно один раз с параметром roomByUpdate
        verify(roomRepository, times(1)).save(roomByUpdate);

        //Проверяем, что id  задан правильно
        assertEquals(roomId, roomByUpdate.getId());
    }

    @Test
    void save_shouldSaveRoom() {
        Room roomToSave = getRoom();

        roomService.save(roomToSave);

        // Метод save должен вызываться ровно один раз
        verify(roomRepository, times(1)).save(roomToSave);
    }

    private Room getRoom() {
        return new Room();
    }
}