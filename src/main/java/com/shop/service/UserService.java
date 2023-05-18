package com.shop.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.shop.dto.UserDto;
import com.shop.exception.BadRequestException;
import com.shop.model.User;
import com.shop.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserDto createUser(UserDto userDto, String password)
        throws NoSuchAlgorithmException {
        if (password.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new BadRequestException("Email " + userDto.getEmail() + " already exists");
        }

        User user = convertToEntity(userDto);
        byte[] salt = createSalt();
        byte[] hashedPassword = createHash(password, salt);
        user.setStoredSalt(salt);
        user.setStoredHash(hashedPassword);
        userRepository.save(user);
        return convertToDto(user);
    }

    private UserDto convertToDto(User entity) {
        return modelMapper.map(entity, UserDto.class);
    }

    private User convertToEntity(UserDto dto) {
        return modelMapper.map(dto, User.class);
    }

    private byte[] createSalt() {
        var random = new SecureRandom();
        var salt = new byte[128];
        random.nextBytes(salt);

        return salt;
    }

    private byte[] createHash(String password, byte[] salt)
        throws NoSuchAlgorithmException {
        var md = MessageDigest.getInstance("SHA-512");
        md.update(salt);

        return md.digest(password.getBytes(StandardCharsets.UTF_8));
    }
}
