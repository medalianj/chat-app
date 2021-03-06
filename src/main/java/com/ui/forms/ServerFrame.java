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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.util.List;

public class ServerFrame extends javax.swing.JFrame {

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
    //ServerBundle serverBundle = null;
    Contact selectedContact = null;
    List<Contact> contacts = null;
    List<Inbox> messages = null;
    DefaultListModel dmContacts = new DefaultListModel();
    DefaultListModel dmMessages = new DefaultListModel();

    IContactManager ContactManager = new ContactManager();
    IInboxManager InboxManager = new InboxManager();

    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    BufferedReader in = null;
    PrintWriter out = null;

    public ServerFrame(Appuser appUser/*, ServerBundle serverBundle*/) throws Exception {
        this.user = appUser;
        //this.serverBundle = serverBundle;
        this.contacts = loadContacts(this.user);
        setUser();
        setContacts();
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActions();
        //initServer();
        instantServerMessagingReceiver();
    }

    private void addActions() {
        var serverFrame = this;
        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource().equals(sendBtn)) {
                    var messageText = messageTextField.getText();
                    if (messageText != null && !messageText.equals("")) {
                        try {
                            //sendMessage(messageText);
                            instantServerMessagingSender(messageText);
                            dmMessages.addElement("Sent : " + messageText);
                            messageTextField.setText("");
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    } else {
                        JOptionPane.showMessageDialog(serverFrame, "The message cannot be empty");
                    }
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
        List contacts = null;
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

    private void instantServerMessagingReceiver() {
        var serverFrame = this;
        Thread receive = new Thread(new Runnable() {
            String msg;

            @Override
            public void run() {
                try {
                    msg = in.readLine();
                    //tant que le client est connect??
                    while (msg != null) {
                        System.out.println("Client : " + msg);
                        msg = in.readLine();
                        dmMessages.addElement("Received : " + msg);
                    }

                    JOptionPane.showMessageDialog(serverFrame, "Client disconnected");

                    out.close();
                    clientSocket.close();
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        receive.start();
    }

    private void instantServerMessagingSender(String message) {
        Thread sender = new Thread(new Runnable() {//variable that will contains the data writter by the user
            String msg = message;
            @Override   // annotation to override the run method
            public void run() {
                while (true) {
                    out.println(msg);// write data stored in msg in the clientSocket
                    out.flush();   // forces the sending of the data
                }
            }
        });
        sender.start();
    }

    private void initServer() throws IOException {

//        Thread serverThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    serverSocket = new ServerSocket(5000);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                try {
//                    clientSocket = serverSocket.accept();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                try {
//                    out = new PrintWriter(clientSocket.getOutputStream());
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                try {
//                    in = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                //serverBundle = new ServerBundle(serverSocket, clientSocket, in, out);
//            }
//        });
//
//        serverThread.start();

    }

}
