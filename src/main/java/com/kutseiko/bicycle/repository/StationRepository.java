package com.kutseiko.bicycle.repository;

import com.kutseiko.bicycle.entity.Station;
import java.util.List;
import java.util.Optional;

public interface StationRepository {

    Optional<Station> getStationById(Long id);

    List<Station> getAllStations();

    Optional<Station> updateStation(Station station);

    boolean deleteStationById(Long id);

    Optional<Station> addStation(Station station);
}
