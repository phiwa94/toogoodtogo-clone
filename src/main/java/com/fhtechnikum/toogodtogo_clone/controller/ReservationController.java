package com.fhtechnikum.toogodtogo_clone.controller;

import com.fhtechnikum.toogodtogo_clone.dto.ReservationCreationDto;
import com.fhtechnikum.toogodtogo_clone.dto.ReservationDto;
import com.fhtechnikum.toogodtogo_clone.dto.ReservationUpdateDto;
import com.fhtechnikum.toogodtogo_clone.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reservations")
@CrossOrigin
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public List<ReservationDto> readAll() {
        return reservationService.findAll();
    }

    @GetMapping("/{id}")
    public ReservationDto read(@PathVariable UUID id) {
        return reservationService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationDto create(@RequestBody @Valid ReservationCreationDto dto) {
        return reservationService.create(dto);
    }

    // Status aendern, z. B. auf PICKED_UP oder CANCELLED
    @PutMapping("/{id}")
    public ReservationDto update(@PathVariable UUID id, @RequestBody @Valid ReservationUpdateDto dto) {
        return reservationService.updateStatus(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        reservationService.delete(id);
    }
}
