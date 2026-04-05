package com.example.house.service.impl.admin;

import com.example.house.model.entity.Room;
import com.example.house.service.admin.AdminDomainService;
import com.example.house.service.admin.RoomManagementService;
import com.example.house.service.impl.admin.AdminDomainServiceImpl;

import java.util.List;

public class RoomManagementServiceImpl implements RoomManagementService {
    private final AdminDomainService workflow;

    public RoomManagementServiceImpl() {
        this(new AdminDomainServiceImpl());
    }

    public RoomManagementServiceImpl(AdminDomainService workflow) {
        this.workflow = workflow;
    }

    @Override
    public List<Room> getRooms() {
        return workflow.getRooms();
    }

    @Override
    public Room saveRoom(Room room) {
        return workflow.saveRoom(room);
    }

    @Override
    public void deleteRoom(Integer roomId) {
        workflow.deleteRoom(roomId);
    }
}

