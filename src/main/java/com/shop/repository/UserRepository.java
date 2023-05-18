package com.shop.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shop.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    
    Boolean existsByEmail(String email);
    User findByEmail(String email);
}
