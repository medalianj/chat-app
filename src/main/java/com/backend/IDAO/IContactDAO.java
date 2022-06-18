package com.backend.IDAO;

import com.tables.entities.Contact;

import java.util.List;

public interface IContactDAO {

    List<Contact> GetListContactByUserId(int idUser);

}
