package com.kutseiko.bicycle.service.impl;

import com.kutseiko.bicycle.DTO.BikeDto;
import com.kutseiko.bicycle.entity.Bike;
import com.kutseiko.bicycle.repository.BikeRepository;
import com.kutseiko.bicycle.service.BikeService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BikeServiceImpl implements BikeService {

    private final BikeRepository bikeRepository;

    @Override
    public Optional<Bike> getBikeById(Long id) {
        return bikeRepository.getBikeById(id);
    }

    @Override
    public List<Bike> getAllBikes() {
        return bikeRepository.getAllBikes();
    }

    @Override
    public Optional<Bike> createBike(BikeDto bikeDto) {
        Bike bike = new Bike().setInfo(bikeDto.getInfo()).setStationId(bikeDto.getStationId());
        return bikeRepository.addBike(bike);
    }

    @Override
    public boolean deleteBikeById(Long id) {
        return bikeRepository.deleteBikeById(id);
    }

    @Override
    public Optional<Bike> updateBike(Long id, BikeDto bikeDto) {
        Bike bike = new Bike().setId(id).setInfo(bikeDto.getInfo()).setStationId(bikeDto.getStationId());
        return bikeRepository.updateBike(bike);
    }
}
