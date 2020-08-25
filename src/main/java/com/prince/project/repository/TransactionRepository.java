package com.prince.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prince.project.model.TransactionHistory;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionHistory, Integer> {
}
