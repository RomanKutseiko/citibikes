package com.kutseiko.bicycle.service;

import com.kutseiko.bicycle.DTO.TripDto;
import com.kutseiko.bicycle.entity.Trip;

import java.util.List;
import java.util.Optional;

public interface TripService {
    
    Optional<Trip> getTripById(Long id);

    List<Trip> getAllTrips();

    Optional<Trip> createTrip(TripDto tripDto);

    boolean deleteTripById(Long id);

    Optional<Trip> updateTrip(Long id, TripDto tripDto);
}
