package com.backend.IManagers;

import com.tables.entities.Contact;

import java.util.List;

public interface IContactManager {

    List<Contact> LoadContactListByIdUser(int idUser) throws Exception;
}
