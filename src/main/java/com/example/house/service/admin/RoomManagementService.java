package com.example.house.service.admin;

import com.example.house.model.entity.Room;

import java.util.List;

public interface RoomManagementService {
    List<Room> getRooms();
    Room saveRoom(Room room);
    void deleteRoom(Integer roomId);
}

