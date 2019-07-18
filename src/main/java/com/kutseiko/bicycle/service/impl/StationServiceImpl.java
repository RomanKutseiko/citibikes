package com.kutseiko.bicycle.service.impl;

import com.kutseiko.bicycle.DTO.StationDto;
import com.kutseiko.bicycle.entity.Station;
import com.kutseiko.bicycle.repository.StationRepository;
import com.kutseiko.bicycle.service.StationService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {

    private final StationRepository stationRepository;

    @Override
    public Optional<Station> getStationById(Long id) {
        return stationRepository.getStationById(id);
    }

    @Override
    public List<Station> getAllStations() {
        return stationRepository.getAllStations();
    }

    @Override
    public Optional<Station> createStation(StationDto stationDto) {
        Station station = new Station().setName(stationDto.getName()).setLatitude(stationDto.getLatitude())
            .setLongitude(stationDto.getLongitude());
        return stationRepository.addStation(station);
    }

    @Override
    public boolean deleteStationById(Long id) {
        return stationRepository.deleteStationById(id);
    }

    @Override
    public Optional<Station> updateStation(Long id, StationDto stationDto) {
        Station station = new Station().setId(id).setName(stationDto.getName()).setLatitude(stationDto.getLatitude())
            .setLongitude(stationDto.getLongitude());
        return stationRepository.updateStation(station);
    }
}
