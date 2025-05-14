package com.example.seating.dto;

import com.example.seating.entity.RoomType;
import lombok.*;

@Data
@NoArgsConstructor
public class RoomDTO {
    private Long id;
    private String roomNumber;
    private Integer capacity;
    private RoomType roomType;
    private Long floorId;

    public RoomDTO(Long id,String roomNumber, Integer capacity, RoomType roomType, Long floorId) {
        this.roomNumber = roomNumber;
        this.capacity = capacity;
        this.roomType = roomType;
        this.floorId = floorId;
        this.id = id;
    }
    public RoomDTO(Long id, String roomNumber, Long floorId) {
        this.roomNumber = roomNumber;
        this.floorId = floorId;
        this.id = id;
    }
}
