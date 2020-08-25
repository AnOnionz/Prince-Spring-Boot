package com.prince.project.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.prince.project.model.Post;
import com.prince.project.model.WatchHistory;


@Repository
@Transactional
public class PostDAO {
	
	@Autowired
    private EntityManager entityManager;
 
    public List<Post> findPostByUser(Integer userId) {
        try {
            String sql = "Select p from " + Post.class.getName() + " p " //
                    + " Where p.author.id = :userId ";
 
            Query query = entityManager.createQuery(sql, Post.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
    public List<Post> findPostRecent(Integer userId) { 
    	
        try {
        	String sql = "Select p from " + Post.class.getName() + " p " //
                    + " Where p.id NOT IN ("+"Select w.post.id from "+WatchHistory.class.getName() + " w " + " where w.user.id = :userId AND w.post.id = p.id AND w.isClick = 1" +" ) "+"AND p.status = 1 AND p.startDate <= current_date and p.endDate >= current_date ORDER BY p.startDate DESC";

            Query query = entityManager.createQuery(sql, Post.class);
            query.setParameter("userId", userId);
            query.setMaxResults(5);
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }
    

}

