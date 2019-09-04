package com.kutseiko.bicycle.controller;

import com.kutseiko.bicycle.DTO.StationDto;
import com.kutseiko.bicycle.entity.Station;
import com.kutseiko.bicycle.service.StationService;
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
@RequestMapping("/stations")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    @GetMapping("/{id}")
    public Optional<Station> getStationByID(@PathVariable(name = "id")Long id) {
        return stationService.getStationById(id);
    }

    @GetMapping
    public List<Station> getAllStations() {
        return stationService.getAllStations();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<Station> createStation(@RequestBody @Valid StationDto stationDto) {
        return stationService.createStation(stationDto);
    }

    @PutMapping("/{id}")
    public Optional<Station> updateStation(@RequestBody @Valid StationDto stationDto, @PathVariable(name = "id")Long id) {
        return stationService.updateStation(id, stationDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean updateStation(@PathVariable(name = "id")Long id) {
        return stationService.deleteStationById(id);
    }

}
