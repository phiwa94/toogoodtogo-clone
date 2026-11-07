package com.fhtechnikum.toogodtogo_clone.service;

import com.fhtechnikum.toogodtogo_clone.dto.UserCreationDto;
import com.fhtechnikum.toogodtogo_clone.dto.UserDto;
import com.fhtechnikum.toogodtogo_clone.entity.UserEntity;
import com.fhtechnikum.toogodtogo_clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> findAll() {
        List<UserDto> result = new ArrayList<>();
        for (UserEntity entity : userRepository.findAll()) {
            result.add(toDto(entity));
        }
        return result;
    }

    public UserDto findById(UUID id) {
        UserEntity user = getUserEntity(id);
        return toDto(user);
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

        // only check if the email has changed (users e-mail is ofc taken already)
        if (!user.getEmail().equals(dto.email())) {
            if (userRepository.existsByEmail(dto.email())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
            }
        }

        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setEmail(dto.email());
        UserEntity saved = userRepository.save(user);
        return toDto(saved);
    }

    public void delete(UUID id) {
        UserEntity user = getUserEntity(id);
        userRepository.delete(user);
    }

    // also used by ReservationService
    // without it spring would return 500 (server error) instead of 404 (not found) for an unknown id
    public UserEntity getUserEntity(UUID id) {
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user.get();
    }

    private UserDto toDto(UserEntity entity) {
        return new UserDto(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getEmail());
    }
}
