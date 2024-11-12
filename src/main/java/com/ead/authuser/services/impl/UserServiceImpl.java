package com.ead.authuser.services.impl;

import com.ead.authuser.exceptions.NotFoundException;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.UserService;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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
}