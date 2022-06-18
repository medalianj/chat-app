package com.backend.Managers;

import com.backend.DAO.ContactDAO;
import com.backend.IDAO.IContactDAO;
import com.backend.IManagers.IContactManager;
import com.tables.entities.Contact;

import java.util.List;

public class ContactManager implements IContactManager {

    IContactDAO ContactDAO = new ContactDAO();

    @Override
    public List<Contact> LoadContactListByIdUser(int idUser) throws Exception {
        try {
            return ContactDAO.GetListContactByUserId(idUser);
        }
        catch(Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }
}
