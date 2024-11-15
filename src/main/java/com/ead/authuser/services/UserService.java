package com.ead.authuser.services;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<UserModel> findAll();
    Page<UserModel> findAll(Pageable pageable);
    Optional<UserModel> findById(UUID id);
    void delete(UserModel userModel);
    UserModel registerUser(UserDto userDto);
    boolean existByUserName(String username);
    boolean existByEmail(String email);
    UserModel updateUser(UserDto userDto, UserModel userModel);
    UserModel updateUserPassword(UserDto userDto, UserModel userModel);
    UserModel updateUserImage(UserDto userDto, UserModel userModel);
}
