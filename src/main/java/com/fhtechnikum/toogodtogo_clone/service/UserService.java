package com.fhtechnikum.toogodtogo_clone.service;

import com.fhtechnikum.toogodtogo_clone.dto.UserCreationDto;
import com.fhtechnikum.toogodtogo_clone.dto.UserDto;
import com.fhtechnikum.toogodtogo_clone.entity.UserEntity;
import com.fhtechnikum.toogodtogo_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    public UserDto findById(UUID id) {
        return toDto(getUserEntity(id));
    }

    public UserDto create(UserCreationDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }
        UserEntity saved = userRepository.save(new UserEntity(dto.firstName(), dto.lastName(), dto.email()));
        return toDto(saved);
    }

    public UserDto update(UUID id, UserCreationDto dto) {
        UserEntity user = getUserEntity(id);
        if (!user.getEmail().equals(dto.email()) && userRepository.existsByEmail(dto.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        return toDto(userRepository.save(user));
    }

    public void delete(UUID id) {
        getUserEntity(id); // wirft 404, falls nicht vorhanden
        userRepository.deleteById(id);
    }

    // Wird auch vom ReservationService verwendet
    public UserEntity getUserEntity(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private UserDto toDto(UserEntity entity) {
        return new UserDto(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getEmail());
    }
}
