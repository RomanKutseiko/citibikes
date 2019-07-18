package com.kutseiko.bicycle.service;

import com.kutseiko.bicycle.DTO.StationDto;
import com.kutseiko.bicycle.entity.Station;
import java.util.List;
import java.util.Optional;

public interface StationService {

    Optional<Station> getStationById(Long id);

    List<Station> getAllStations();

    Optional<Station> createStation(StationDto stationDto);

    boolean deleteStationById(Long id);

    Optional<Station> updateStation(Long id, StationDto stationDto);
}
