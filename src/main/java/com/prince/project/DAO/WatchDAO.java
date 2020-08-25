package com.prince.project.DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.prince.project.model.WatchHistory;
@Repository
@Transactional
public class WatchDAO {
	@Autowired
    private EntityManager entityManager;
 
    public WatchHistory getWatchHistory(Integer userId, Integer postId) {
    	try {
        String sql = "Select w from " + WatchHistory.class.getName() + " w " //
                + " where w.user.id = :userId AND w.post.id = :postId ";
 
        Query query = this.entityManager.createQuery(sql, WatchHistory.class);
        query.setParameter("userId", userId);
        query.setParameter("postId", postId);
        return (WatchHistory) query.getSingleResult();
    	}catch (NoResultException e){
        	return null;
        }
    }
    public List<String> getlistPostWatched(Integer userId) {
    	try {
        String sql = "Select w.post.id from " + WatchHistory.class.getName() + " w " //
                + " where w.user.id = :userId AND w.isclick = 1";
 
        Query query = this.entityManager.createQuery(sql, WatchHistory.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    	}catch (NoResultException e) {
    		return null;
    	}
    }
 

}
