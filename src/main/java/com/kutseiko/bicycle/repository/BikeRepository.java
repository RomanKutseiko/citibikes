package com.kutseiko.bicycle.repository;

import com.kutseiko.bicycle.entity.Bike;
import java.util.List;
import java.util.Optional;

public interface BikeRepository {

    Optional<Bike> getBikeById(Long id);

    List<Bike> getAllBikes();

    Optional<Bike> updateBike(Bike bike);

    boolean deleteBikeById(Long id);

    Optional<Bike> addBike(Bike bike);

}
