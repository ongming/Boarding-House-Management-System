package com.example.house.repository.staff;

import com.example.house.model.entity.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    List<Room> findAllOrderByFloorAndRoomNumber();

    Optional<Room> findById(Integer id);

    Optional<Room> findByRoomNumber(String roomNumber);

    Room save(Room room);

    void deleteById(Integer id);
}


