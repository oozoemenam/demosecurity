package com.shop.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class UserDto {
    private UUID id;
    private String email;
    private String mobile;
}
