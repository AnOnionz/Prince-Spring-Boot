package com.prince.project.DAO;

import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.prince.project.model.TransactionHistory;

@Repository
@Transactional
public class TranDAO {
	@Autowired
    private EntityManager entityManager;
 
    public List<TransactionHistory> findTransactionByUser(Integer userId) {
        try {
            String sql = "Select t from " + TransactionHistory.class.getName() + " t " //
                    + " Where t.author.id = :userId ";
 
            Query query = entityManager.createQuery(sql, TransactionHistory.class);
            query.setParameter("userId", userId);
 
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

}
