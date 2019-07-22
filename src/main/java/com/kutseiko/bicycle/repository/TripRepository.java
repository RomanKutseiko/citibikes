package com.kutseiko.bicycle.repository;

import com.kutseiko.bicycle.entity.Trip;
import java.util.List;
import java.util.Optional;

public interface TripRepository {

    Optional<Trip> getTripById(Long id);

    List<Trip> getAllTrips();

    Optional<Trip> updateTrip(Trip trip);

    boolean deleteTripById(Long id);

    Optional<Trip> addTrip(Trip trip);
}
