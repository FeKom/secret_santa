package com.github.fekom.secret_santa.service;

import com.github.fekom.secret_santa.entity.UserEntity;
import com.github.fekom.secret_santa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public List<UserEntity> findAll() {
        return repository.findAll();
    }

}
