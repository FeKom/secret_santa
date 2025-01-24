package com.github.fekom.secret_santa.repository;

import com.github.fekom.secret_santa.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String>{
   // Optional<UserModel> findByEmail(String email);
}
