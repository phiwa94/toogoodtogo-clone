package com.fhtechnikum.toogodtogo_clone.controller;

import com.fhtechnikum.toogodtogo_clone.dto.ReservationDto;
import com.fhtechnikum.toogodtogo_clone.dto.UserCreationDto;
import com.fhtechnikum.toogodtogo_clone.dto.UserDto;
import com.fhtechnikum.toogodtogo_clone.service.ReservationService;
import com.fhtechnikum.toogodtogo_clone.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ReservationService reservationService;

    @GetMapping
    public List<UserDto> readAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto read(@PathVariable UUID id) {
        return userService.findById(id);
    }

    // sub resource: all reservations of a user
    @GetMapping("/{id}/reservations")
    public List<ReservationDto> readReservations(@PathVariable UUID id) {
        return reservationService.findByUser(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid UserCreationDto dto) {
        return userService.create(dto);
    }

    @PutMapping("/{id}")
    public UserDto update(@PathVariable UUID id, @RequestBody @Valid UserCreationDto dto) {
        return userService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        userService.delete(id);
    }
}
