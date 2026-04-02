package com.example.house.view.staff.home;

import com.example.house.model.entity.Room;
import com.example.house.model.enums.RoomStatus;
import com.example.house.repository.impl.JpaRoomRepository;
import com.example.house.repository.staff.RoomRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

public class StaffHomeCatalog {
    private final RoomRepository roomRepository;
    private final ObservableList<StaffRoomSnapshot> rooms = FXCollections.observableArrayList();

    public StaffHomeCatalog() {
        this(new JpaRoomRepository());
    }

    public StaffHomeCatalog(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
        loadRooms();
    }

    public ObservableList<StaffRoomSnapshot> getRooms() {
        return rooms;
    }

    public List<Integer> getFloors() {
        return rooms.stream()
                .map(StaffRoomSnapshot::floor)
                .distinct()
                .sorted(Comparator.naturalOrder())
                .toList();
    }

    private void loadRooms() {
        try {
            List<Room> roomEntities = roomRepository.findAllOrderByFloorAndRoomNumber();
            rooms.setAll(roomEntities.stream().map(this::toSnapshot).toList());
        } catch (Exception ex) {
            rooms.clear();
        }
    }

    private StaffRoomSnapshot toSnapshot(Room room) {
        RoomStatus status = room.getStatus() == null ? RoomStatus.TRONG : room.getStatus();
        int floor = room.getFloor() == null ? 0 : room.getFloor();
        double size = room.getSize() == null ? 0.0 : room.getSize().doubleValue();
        BigDecimal baseRent = room.getBaseRent() == null ? BigDecimal.ZERO : room.getBaseRent();
        String furniture = room.getFurnitureList() == null ? "" : room.getFurnitureList();

        String tenantName = status == RoomStatus.DA_THUE ? "Đang có người thuê" : null;
        Integer occupantCount = status == RoomStatus.DA_THUE ? 1 : null;

        return new StaffRoomSnapshot(
                room.getRoomNumber(),
                floor,
                size,
                baseRent,
                furniture,
                status,
                tenantName,
                occupantCount
        );
    }
}
