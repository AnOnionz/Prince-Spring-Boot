package com.prince.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prince.project.model.AppUser;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> {
 
}
