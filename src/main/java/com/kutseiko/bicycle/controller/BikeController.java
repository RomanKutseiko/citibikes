package com.kutseiko.bicycle.controller;

import com.kutseiko.bicycle.DTO.BikeDto;
import com.kutseiko.bicycle.entity.Bike;
import com.kutseiko.bicycle.service.BikeService;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bikes")
@RequiredArgsConstructor
public class BikeController {

    private final BikeService bikeService;

    @GetMapping("/{id}")
    public Optional<Bike> getBikeByID(@PathVariable(name = "id")Long id) {
        return bikeService.getBikeById(id);
    }

    @GetMapping
    public List<Bike> getAllBikes() {
        return bikeService.getAllBikes();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<Bike> createBike(@RequestBody @Valid BikeDto bikeDto) {
        return bikeService.createBike(bikeDto);
    }

    @PutMapping("/{id}")
    public Optional<Bike> updateBike(@RequestBody @Valid BikeDto bikeDto, @PathVariable(name = "id")Long id) {
        return bikeService.updateBike(id, bikeDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean updateBike(@PathVariable(name = "id")Long id) {
        return bikeService.deleteBikeById(id);
    }

}
