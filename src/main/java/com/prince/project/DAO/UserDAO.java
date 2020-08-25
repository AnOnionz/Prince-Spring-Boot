package com.prince.project.DAO;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.prince.project.model.AppUser;


@Repository
@Transactional
public class UserDAO {
	
	@Autowired
    private EntityManager entityManager;
 
    public AppUser findUserAccountByEmail(String email) {
        try {
            String sql = "Select e from " + AppUser.class.getName() + " e " //
                    + " Where e.email = :email ";
 
            Query query = entityManager.createQuery(sql, AppUser.class);
            query.setParameter("email", email);
 
            return (AppUser) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
	public AppUser findUserAccountByName(String username) {
		
		AppUser user;
		
		try {
			user = (AppUser) entityManager.createQuery("from AppUser u where u.name = :username")
					.setParameter("username", username)
					.getSingleResult();
		} catch (NoResultException e) {
			user = null;
		}
		
		return user;
	}
    public AppUser findUserAccountByToken(String token) {
        try {
            String sql = "Select e from " + AppUser.class.getName() + " e " //
                    + " Where e.emailVerifyHash = :token ";
 
            Query query = entityManager.createQuery(sql, AppUser.class);
            query.setParameter("token", token);
 
            return (AppUser) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
