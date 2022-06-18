package com.backend.Managers;

import com.backend.DAO.InboxDAO;
import com.backend.IDAO.IInboxDAO;
import com.backend.IManagers.IInboxManager;
import com.tables.entities.Inbox;

import java.util.List;

public class InboxManager implements IInboxManager {

    IInboxDAO InboxDAO = new InboxDAO();

    @Override
    public List<Inbox> LoadInboxListByMessageHeader(String messageHeaderUserContactFormat, String messageHeaderContactUserFormati) throws Exception {
        try {
            return InboxDAO.GetInboxListByMessageHeader(messageHeaderUserContactFormat, messageHeaderContactUserFormati);
        }
        catch(Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }

    @Override
    public void SaveSentMessage(Inbox message) throws Exception {
        try {
            InboxDAO.SaveSentMessage(message);
        }
        catch(Exception exception) {
            throw new Exception(exception.getMessage());
        }
    }
}
