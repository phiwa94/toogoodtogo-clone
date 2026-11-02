package com.fhtechnikum.toogodtogo_clone.controller;

import com.fhtechnikum.toogodtogo_clone.dto.OfferDto;
import com.fhtechnikum.toogodtogo_clone.dto.StoreCreationDto;
import com.fhtechnikum.toogodtogo_clone.dto.StoreDto;
import com.fhtechnikum.toogodtogo_clone.service.OfferService;
import com.fhtechnikum.toogodtogo_clone.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/stores")
@CrossOrigin
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final OfferService offerService;

    // GET /stores  oder  GET /stores?city=Wien  (Filtern ueber Query-Parameter)
    @GetMapping
    public List<StoreDto> readAll(@RequestParam(required = false) String city) {
        return storeService.findAll(city);
    }

    @GetMapping("/{id}")
    public StoreDto read(@PathVariable UUID id) {
        return storeService.findById(id);
    }

    // Sub-Resource: alle Angebote eines Stores
    @GetMapping("/{id}/offers")
    public List<OfferDto> readOffers(@PathVariable UUID id) {
        return offerService.findByStore(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StoreDto create(@RequestBody @Valid StoreCreationDto dto) {
        return storeService.create(dto);
    }

    @PutMapping("/{id}")
    public StoreDto update(@PathVariable UUID id, @RequestBody @Valid StoreCreationDto dto) {
        return storeService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        storeService.delete(id);
    }
}
