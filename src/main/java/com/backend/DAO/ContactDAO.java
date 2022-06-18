package com.backend.DAO;

import com.backend.IDAO.IContactDAO;
import com.tables.entities.Contact;

import javax.persistence.*;
import java.util.List;

public class ContactDAO implements IContactDAO {
    @Override
    public List<Contact> GetListContactByUserId(int idUser) {

        List<Contact> contactList = null;

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            Query query = entityManager.createNativeQuery("SELECT * FROM Contact c where c.iduser=:userId", Contact.class);
            query.setParameter("userId", idUser);

            if(query.getResultList() != null && query.getResultList().size() > 0) {
                contactList = query.getResultList();
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
        return contactList;
    }
}
