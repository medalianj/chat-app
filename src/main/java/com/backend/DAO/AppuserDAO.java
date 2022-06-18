package com.backend.DAO;

import com.backend.IDAO.IAppUserDAO;
import com.tables.entities.Appuser;

import javax.persistence.*;

public class AppuserDAO implements IAppUserDAO {

    @Override
    public Appuser GetAppUserByUsernameAndPassword(String username, String password) {

        Appuser user = null;
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            Query userAthentified = entityManager.createNativeQuery("SELECT Count(*) FROM Appuser au where au.username=:userN AND au.passworduser=:password");
            userAthentified.setParameter("userN", username);
            userAthentified.setParameter("password", password);
            var count = userAthentified.getSingleResult() != null ? Integer.valueOf(userAthentified.getSingleResult().toString()) : 0 ;
            if(count == 0) {
                System.out.println("The user doesn't exist");
            }
            else {
                userAthentified = entityManager.createNativeQuery("SELECT * FROM Appuser au where au.username=:userN AND au.passworduser=:password", Appuser.class);
                userAthentified.setParameter("userN", username);
                userAthentified.setParameter("password", password);
                user = (Appuser) userAthentified.getSingleResult();
                System.out.println("The user is " + user.getUsername());
            }

            transaction.commit();
        }
        finally {
            if(transaction.isActive()) {
                transaction.rollback();
            }
            entityManager.close();
            entityManagerFactory.close();
        }
        return user;
    }

}
