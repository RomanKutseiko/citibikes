package com.kutseiko.bicycle.service;

import com.kutseiko.bicycle.DTO.BikeDto;
import com.kutseiko.bicycle.entity.Bike;
import java.util.List;
import java.util.Optional;

public interface BikeService {

    Optional<Bike> getBikeById(Long id);

    List<Bike> getAllBikes();

    Optional<Bike> createBike(BikeDto bikeDto);

    boolean deleteBikeById(Long id);

    Optional<Bike> updateBike(Long id, BikeDto bikeDto);

}
