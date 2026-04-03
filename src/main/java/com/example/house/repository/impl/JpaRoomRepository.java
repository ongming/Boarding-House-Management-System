package com.example.house.repository.impl;

import com.example.house.model.entity.Room;
import com.example.house.repository.staff.RoomRepository;

import java.util.List;
import java.util.Optional;

public class JpaRoomRepository extends JpaRepositorySupport implements RoomRepository {
    @Override
    public List<Room> findAllOrderByFloorAndRoomNumber() {
        return withEntityManager(em -> em.createQuery(
                        "SELECT r FROM Room r ORDER BY r.floor ASC, r.roomNumber ASC", Room.class)
                .getResultList());
    }

    @Override
    public Optional<Room> findById(Integer id) {
        return withEntityManager(em -> Optional.ofNullable(em.find(Room.class, id)));
    }

    @Override
    public Optional<Room> findByRoomNumber(String roomNumber) {
        return withEntityManager(em -> em.createQuery(
                        "SELECT r FROM Room r WHERE LOWER(r.roomNumber) = LOWER(:roomNumber)", Room.class)
                .setParameter("roomNumber", roomNumber)
                .getResultStream()
                .findFirst());
    }

    @Override
    public Room save(Room room) {
        return inTransaction(em -> {
            if (room.getId() == null) {
                em.persist(room);
                return room;
            }
            return em.merge(room);
        });
    }

    @Override
    public void deleteById(Integer id) {
        inTransaction(em -> {
            Room room = em.find(Room.class, id);
            if (room != null) {
                em.remove(room);
            }
            return null;
        });
    }
}

