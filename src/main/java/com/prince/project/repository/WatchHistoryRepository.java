package com.prince.project.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prince.project.model.WatchHistory;

	@Repository
	public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Integer> {
	
}
