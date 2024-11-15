package com.ead.authuser.services.impl;

import com.ead.authuser.dto.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.exceptions.NotFoundException;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<UserModel> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<UserModel> findById(UUID id) {
        Optional<UserModel> userModeloptional = userRepository.findById(id);
        if (userModeloptional.isEmpty()) {
            throw new NotFoundException("Error: User not found");
        }
        return userModeloptional;
    }

    @Override
    public void delete(UserModel userModel) {
        userRepository.delete(userModel);
    }

    @Override
    public UserModel registerUser(UserDto userDto) {
        var userModel = new UserModel();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.USER);
        userModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return userRepository.save(userModel);
    }

    @Override
    public boolean existByUserName(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserModel updateUser(UserDto userDto, UserModel userModel) {
        userModel.setFullName(userDto.fullName());
        userModel.setPhoneNumber(userDto.phoneNumber());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return userRepository.save(userModel);
    }

    @Override
    public UserModel updateUserPassword(UserDto userDto, UserModel userModel) {
        userModel.setPassword(userDto.password());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return userRepository.save(userModel);
    }

    @Override
    public UserModel updateUserImage(UserDto userDto, UserModel userModel) {
        userModel.setImageUrl(userDto.imageUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return userRepository.save(userModel);
    }
}
