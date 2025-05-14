package com.example.seating.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.seating.dto.RoomDTO;
import com.example.seating.entity.Floor;
import com.example.seating.entity.Room;
import com.example.seating.entity.RoomType;
import com.example.seating.repository.FloorRepository;
import com.example.seating.repository.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final FloorRepository floorRepository;

    public RoomDTO createRoom(RoomDTO roomDTO) {
        // Check if room already exists in the floor
        if (roomRepository.existsByRoomNumberAndFloorId(roomDTO.getRoomNumber(), roomDTO.getFloorId())) {
            throw new RuntimeException("Room " + roomDTO.getRoomNumber() + " already exists in this floor!");
        }

        Room room = convertToEntity(roomDTO);
        Room savedRoom = roomRepository.save(room);
        return convertToDTO(savedRoom);
    }

    public RoomDTO updateRoom(Long id, RoomDTO roomDTO) {
        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Check if the new room number already exists in the same floor (if changed)
        if (!existingRoom.getRoomNumber().equals(roomDTO.getRoomNumber()) &&
            roomRepository.existsByRoomNumberAndFloorId(roomDTO.getRoomNumber(), existingRoom.getFloor().getId())) {
            throw new RuntimeException("Room number already exists in this floor!");
        }

        existingRoom.setRoomNumber(roomDTO.getRoomNumber());
        existingRoom.setRoomType(roomDTO.getRoomType());
        existingRoom.setCapacity(calculateCapacity(roomDTO.getRoomType()));

        Room updatedRoom = roomRepository.save(existingRoom);
        return convertToDTO(updatedRoom);
    }

    public List<RoomDTO> getRoomsByFloorId(Long floorId) {
        return roomRepository.findByFloorId(floorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    private Room convertToEntity(RoomDTO roomDTO) {
        // Calculate capacity based on room type
        int capacity = calculateCapacity(roomDTO.getRoomType());
        
        Floor floor = floorRepository.findById(roomDTO.getFloorId())
                .orElseThrow(() -> new RuntimeException("Floor not found"));
        
        // Ensure room number is properly formatted with block prefix
        String roomNumber = roomDTO.getRoomNumber();
        
        return Room.builder()
                .id(roomDTO.getId())
                .roomNumber(roomNumber)
                .roomType(roomDTO.getRoomType())
                .capacity(capacity)
                .floor(floor)
                .build();
    }

    private int calculateCapacity(RoomType roomType) {
        return roomType == RoomType.ROOM_8X8 ? 64 : 96;
    }

    private RoomDTO convertToDTO(Room room) {
        return new RoomDTO(
                room.getId(),
                room.getRoomNumber(),
                room.getCapacity(),    // Include capacity in DTO
                room.getRoomType(),
                room.getFloor().getId()
        );
    }
}