package com.backend.IManagers;

import com.tables.entities.Inbox;

import java.util.List;

public interface IInboxManager {

    List<Inbox> LoadInboxListByMessageHeader(String messageHeaderUserContactFormat, String messageHeaderContactUserFormat) throws Exception;

    void SaveSentMessage(Inbox message) throws Exception;
}
