package com.kutseiko.bicycle.service.impl;

import com.kutseiko.bicycle.DTO.TripDto;
import com.kutseiko.bicycle.entity.Bike;
import com.kutseiko.bicycle.entity.Station;
import com.kutseiko.bicycle.entity.Trip;
import com.kutseiko.bicycle.entity.User;
import com.kutseiko.bicycle.repository.TripRepository;
import com.kutseiko.bicycle.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;

    @Override
    public Optional<Trip> getTripById(Long id) {
        return tripRepository.getTripById(id);
    }

    @Override
    public List<Trip> getAllTrips() {
        return tripRepository.getAllTrips();
    }

    @Override
    public Optional<Trip> createTrip(TripDto tripDto) {
        Trip trip = new Trip()
            .setUser(new User().setId(tripDto.getUserId()))
            .setBike(new Bike().setId(tripDto.getBikeId()))
            .setStartStation(new Station().setId(tripDto.getStartStationId()))
            .setEndStation(new Station().setId(tripDto.getEndStationId()))
            .setStartTime(tripDto.getStartTime())
            .setEndTime(tripDto.getEndTime());
        return tripRepository.addTrip(trip);
    }

    @Override
    public boolean deleteTripById(Long id) {
        return tripRepository.deleteTripById(id);
    }

    @Override
    public Optional<Trip> updateTrip(Long id, TripDto tripDto) {
        Trip trip = new Trip()
                .setId(id)
                .setUser(new User().setId(tripDto.getUserId()))
                .setBike(new Bike().setId(tripDto.getBikeId()))
                .setStartStation(new Station().setId(tripDto.getStartStationId()))
                .setEndStation(new Station().setId(tripDto.getEndStationId()))
                .setStartTime(tripDto.getStartTime())
                .setEndTime(tripDto.getEndTime());
        return tripRepository.updateTrip(trip);
    }
}
