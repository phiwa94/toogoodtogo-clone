package com.fhtechnikum.toogodtogo_clone.controller;

import com.fhtechnikum.toogodtogo_clone.dto.OfferCreationDto;
import com.fhtechnikum.toogodtogo_clone.dto.OfferDto;
import com.fhtechnikum.toogodtogo_clone.service.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/offers")
@CrossOrigin
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @GetMapping
    public List<OfferDto> readAll() {
        return offerService.findAll();
    }

    @GetMapping("/{id}")
    public OfferDto read(@PathVariable UUID id) {
        return offerService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OfferDto create(@RequestBody @Valid OfferCreationDto dto) {
        return offerService.create(dto);
    }

    @PutMapping("/{id}")
    public OfferDto update(@PathVariable UUID id, @RequestBody @Valid OfferCreationDto dto) {
        return offerService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        offerService.delete(id);
    }
}
