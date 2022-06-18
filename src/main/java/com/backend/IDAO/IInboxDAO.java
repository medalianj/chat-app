package com.backend.IDAO;

import com.tables.entities.Inbox;

import java.util.List;

public interface IInboxDAO {

    List<Inbox> GetInboxListByMessageHeader(String messageHeaderUserContactFormat, String messageHeaderContactUserFormat);

    void SaveSentMessage(Inbox message);

}
