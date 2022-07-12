package com.ui.forms;

import com.backend.IManagers.IContactManager;
import com.backend.IManagers.IInboxManager;
import com.backend.Managers.ContactManager;
import com.backend.Managers.InboxManager;
import com.models.DTO.MessageDTO;
import com.server.classes.Server;
import com.tables.entities.Appuser;
import com.tables.entities.Contact;
import com.tables.entities.Inbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ClientFrame extends javax.swing.JFrame {

    Container container = getContentPane();
    JLabel userLabel = new JLabel("USERNAME");
    JLabel labelContactList = new JLabel("Contact list : ");
    JLabel labelMessagesList = new JLabel("Conversation ");
    JList<Contact> contactList = new JList<>();
    JList<Inbox> messagesList = new JList<>();
    JScrollPane scrollPane = new JScrollPane();
    JTextField messageTextField = new JTextField();
    JButton sendBtn = new JButton("Send");

    //Attributes
    Appuser user = null;
    Contact selectedContact = null;
    java.util.List<Contact> contacts = null;
    java.util.List<Inbox> messages = null;
    DefaultListModel dmContacts = new DefaultListModel();
    DefaultListModel dmMessages = new DefaultListModel();

    IContactManager ContactManager = new ContactManager();
    IInboxManager InboxManager = new InboxManager();

    Server server = null;
    Socket clientSocket = null; // socket used by client to send and receive data from server
    ObjectInputStream in = null;   // object to read data from socket
    ObjectOutputStream out = null;     // object to write data into socket

    public ClientFrame(Appuser appUser, Server server) throws Exception {
        this.user = appUser;
        this.server = server;
        this.contacts = loadContacts(this.user) != null ? loadContacts(this.user) : new ArrayList<>();
        setUser();
        setContacts();
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActions();
        setUpServer(server);
        establishClientConnectionToServer();
        instantClientMessagingReceiver();
    }

    private void addActions() {
        var serverFrame = this;
        sendBtn.addActionListener(e -> {
            if (e.getSource().equals(sendBtn)) {
                var messageText = messageTextField.getText();
                if (messageText != null && !messageText.equals("")) {
                    try {
                        //sendMessage(messageText);
                        instantClientMessagingSender(messageText);
                        dmMessages.addElement("Sent : " + messageText);
                        messageTextField.setText("");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(serverFrame, "The message cannot be empty");
                }
            }
        });
    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        userLabel.setBounds(5, 0, 150, 30);
        labelContactList.setBounds(15, 15, 100, 30);
        contactList.setBounds(5, 40, 200, 510);
        labelMessagesList.setBounds(230, 15, 300, 30);
        messagesList.setBounds(230, 40, 350, 510);
        messageTextField.setBounds(5, 560, 450, 35);
        sendBtn.setBounds(470, 560, 100, 35);
        sendBtn.setEnabled(false);
    }

    private void setUser() {
        userLabel.setText(userLabel.getText() + " : " + user.getUsername());
    }

    private void setContacts() {
        contactList.setModel(dmContacts);
        for (Contact c : contacts) {
            dmContacts.addElement(c.getUsername());
        }
        scrollPane = new JScrollPane(contactList);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        contactList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                messageTextField.setText("");
                var selectedIndex = contactList.getSelectedIndex();
                if (selectedIndex >= 0) {
                    selectedContact = contacts.get(selectedIndex);
                    labelMessagesList.setText("Conversation with " + selectedContact.getFirstName() + " " + selectedContact.getFamilyName());
                    sendBtn.setEnabled(true);
                    //UC user_usernameTOcontact_username
                    //CU contact_usernameTOuser_username
                    var uc = user.getUsername() + "TO" + selectedContact.getUsername();
                    var cu = selectedContact.getUsername() + "TO" + user.getUsername();

                    try {
                        messages = loadMessages(uc, cu);
                        setMessages();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

    }

    private void setMessages() {
        //messagesList = new JList(messages.stream().map(message -> message.getMessageBody()).collect(Collectors.toList()).toArray());
        dmMessages = new DefaultListModel();
        messagesList.setModel(dmMessages);
        for (Inbox message : messages) {
            var UCValue = user.getUsername() + "TO" + selectedContact.getUsername();

            if (message.getMessageHeader().equals(UCValue)) {
                dmMessages.addElement("Sent : " + message.getMessageBody());
            } else {
                dmMessages.addElement("Received : " + message.getMessageBody());
            }
        }

    }

    public void addComponentsToContainer() {
        container.add(scrollPane);
        container.add(userLabel);
        container.add(labelContactList);
        container.add(contactList);
        container.add(labelMessagesList);
        container.add(messagesList);
        container.add(messageTextField);
        container.add(sendBtn);
    }

    private List<Contact> loadContacts(Appuser user) throws Exception {
        List<Contact> contacts = null;
        if (user != null && user.getId() != null) {
            contacts = ContactManager.LoadContactListByIdUser(user.getId());
        }
        return contacts;
    }

    private List<Inbox> loadMessages(String headerUC, String headerCU) throws Exception {
        List<Inbox> messages = null;

        messages = InboxManager.LoadInboxListByMessageHeader(headerUC, headerCU);

        return messages;
    }

    private void sendMessage(String sentMessage) throws Exception {
        var message = new Inbox();
        message.setContact(selectedContact);
        //The message header is a field that defines who sent the message to who
        //It's always written this way: sender_Username + "TO" + receiver_Username
        message.setMessageHeader(user.getUsername() + "TO" + selectedContact.getUsername());
        message.setMessageBody(sentMessage);
        message.setMessageDate(Instant.now());

        InboxManager.SaveSentMessage(message);

    }

    private void instantClientMessagingSender(String messageText) {
        Thread sender = new Thread(new Runnable() {
            final String msgText = messageText;
            final String msgHeader = user.getUsername() + "TO" + selectedContact.getUsername();
            final MessageDTO message = new MessageDTO(msgText, msgHeader);

            @Override
            public void run() {
//                while(true){
//                    try {
//                        out.writeObject(message);
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    try {
//                        out.flush();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }

                try {
                    out.writeObject(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    out.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        sender.start();
    }

    private void instantClientMessagingReceiver() throws InterruptedException {

        Thread receiver = new Thread(new Runnable() {
            MessageDTO message;

            @Override
            public void run() {
                try {
                    System.out.println("Receiving - receiver");
                    message = (MessageDTO) in.readObject();
                    System.out.println("fu - receiver");
                    while (message != null && message.getMessageHeader().equals(selectedContact.getUsername() + "TO" + user.getUsername())) {
                        System.out.println("From Server : " + message.getMessageHeader() + " " + message.getMessageContent());
                        message = (MessageDTO) in.readObject();
                        if (message.getMessageHeader() != null && message.getMessageContent() != null) {
                            dmMessages.addElement("Received : " + message.getMessageContent());
                        }
                    }
                    System.out.println("Server out of service");
                    out.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread.sleep(4000);
        receiver.start();
    }

    private void establishClientConnectionToServer() throws InterruptedException {
        Thread clientConnection = new Thread(() -> {
            try {
                System.out.println("Xxxxxxxxx - clientConnection");
                clientSocket = new Socket("127.0.0.1", 5300);
                System.out.println("Yyyyyyyyyy - clientConnection");
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                System.out.println("Zzzzzzzzzzz - clientConnection");
                in = new ObjectInputStream(clientSocket.getInputStream());
                System.out.println("Connecting looooollll - clientConnection");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        clientConnection.sleep(2000);
        clientConnection.start();
    }


    private void setUpServer(Server server) {
        Thread serverThread = new Thread(server);
        serverThread.start();
    }

}
