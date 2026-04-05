package com.example.house.service.staff;

import com.example.house.model.entity.Contract;

public interface OccupancyService {
    Contract updateOccupancy(String roomNumber, int peopleCount);
}


