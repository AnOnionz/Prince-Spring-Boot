package com.prince.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prince.project.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
}
