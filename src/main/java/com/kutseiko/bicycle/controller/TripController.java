package com.kutseiko.bicycle.controller;

import com.kutseiko.bicycle.DTO.TripDto;
import com.kutseiko.bicycle.entity.Trip;
import com.kutseiko.bicycle.exception.EntityNotFoundException;
import com.kutseiko.bicycle.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @GetMapping("/{id}")
    public Trip getTripByID(@PathVariable(name = "id")Long id) {
        return tripService.getTripById(id).orElseThrow(EntityNotFoundException::new);
    }

    @GetMapping
    public List<Trip> getAllTrips() {
        return tripService.getAllTrips();
    }

    @PostMapping
    public Trip createTrip(@RequestBody @Valid TripDto TripDto) {
        return tripService.createTrip(TripDto).orElseThrow(EntityNotFoundException::new);
    }

    @PutMapping("/{id}")
    public Trip updateTrip(@RequestBody @Valid TripDto TripDto, @PathVariable(name = "id")Long id) {
        return tripService.updateTrip(id, TripDto).orElseThrow(EntityNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    public boolean updateTrip(@PathVariable(name = "id")Long id) {
        return tripService.deleteTripById(id);
    }

}
