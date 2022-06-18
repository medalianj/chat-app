package com.backend.IManagers;

import com.tables.entities.Inbox;

import java.util.List;

public interface IInboxManager {

    List<Inbox> LoadInboxListByMessageHeader(String messageHeaderUserContactFormat, String messageHeaderContactUserFormati) throws Exception;

    void SaveSentMessage(Inbox message) throws Exception;
}
