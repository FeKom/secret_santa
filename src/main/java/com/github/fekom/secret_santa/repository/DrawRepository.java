package com.github.fekom.secret_santa.repository;

import com.github.fekom.secret_santa.entity.DrawEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrawRepository extends JpaRepository<DrawEntity, Long> {
}
