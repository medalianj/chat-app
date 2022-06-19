package com.ui.forms;

import com.backend.IManagers.IContactManager;
import com.backend.IManagers.IInboxManager;
import com.backend.Managers.ContactManager;
import com.backend.Managers.InboxManager;
import com.tables.entities.Appuser;
import com.tables.entities.Contact;
import com.tables.entities.Inbox;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ServerFrame extends javax.swing.JFrame {

    Container container = getContentPane();
    JLabel userLabel = new JLabel("USERNAME");
    JLabel labelContactList = new JLabel("Contact list : ");
    JLabel labelMessagesList = new JLabel("Conversation with ");
    JList contactList = new JList();
    JList messagesList = new JList();

    //Attributes
    Appuser user = null;
    List<Contact> contacts = null;
    List<Inbox> messages = null;
    DefaultListModel dm = new DefaultListModel();

    IContactManager ContactManager = new ContactManager();
    IInboxManager InboxManager = new InboxManager();

    public ServerFrame(Appuser appUser) throws Exception {
        this.user = appUser;
        this.contacts = loadContacts(this.user);
        setUser();
        setContacts();
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        userLabel.setBounds(5, 0, 150, 30);
        labelContactList.setBounds(15, 15, 100, 30);
        contactList.setBounds(5, 40, 200, 510);
        labelMessagesList.setBounds(230, 15, 200, 30);
        messagesList.setBounds(230, 40, 250, 510);
    }

    private void setUser() {
        userLabel.setText(userLabel.getText() + " : " + user.getUsername());
    }

    private void setContacts() {
        contactList.setModel(dm);

        for(Contact c : contacts) {
            dm.addElement(c.getUsername());
        }

    }

    private void setMessages() {
        messagesList.setModel(dm);

        for(Inbox i : messages) {
            dm.addElement(i.getMessageBody());
        }

    }

    public void addComponentsToContainer() {
        container.add(userLabel);
        container.add(labelContactList);
        container.add(contactList);
        container.add(labelMessagesList);
        container.add(messagesList);
    }

    private List<Contact> loadContacts(Appuser user) throws Exception {
        List contacts = null;
        if(user != null && user.getId() != null) {
            contacts = ContactManager.LoadContactListByIdUser(user.getId());
        }
        return contacts;
    }

    private List<Inbox> loadMessages(String headerUC, String headerCU) throws Exception {
        List<Inbox> messages = null;

        messages = InboxManager.LoadInboxListByMessageHeader(headerUC, headerCU);

        return messages;
    }
}
