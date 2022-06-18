package com.backend.DAO;

import com.backend.IDAO.IInboxDAO;
import com.tables.entities.Inbox;

import javax.persistence.*;
import java.util.List;

public class InboxDAO implements IInboxDAO {

    @Override
    public List<Inbox> GetInboxListByMessageHeader(String messageHeaderUserContactFormat, String messageHeaderContactUserFormat) {

        List<Inbox> inboxList = null;

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();

            Query query = entityManager.createNativeQuery("SELECT * FROM Inbox i where i.messageheader=:msgHeadUC or i.messageheader=:msgHeadCU order by messagedate asc", Inbox.class);
            query.setParameter("msgHeadUC", messageHeaderUserContactFormat);
            query.setParameter("msgHeadCU", messageHeaderContactUserFormat);

            if(query.getResultList() != null && query.getResultList().size() > 0) {
                inboxList = query.getResultList();
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
        return inboxList;
    }

    @Override
    public void SaveSentMessage(Inbox message) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(message);
            transaction.commit();
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            entityManager.close();
            entityManagerFactory.close();
        }
    }

}
