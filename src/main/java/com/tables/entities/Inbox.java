package com.tables.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "inbox")
public class Inbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idinbox", nullable = false)
    private Integer id;

    @Column(name = "messagebody", nullable = false, length = 2500)
    private String messageBody;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idcontact")
    private Contact contact;

    @Column(name = "messageheader", nullable = false, length = 500)
    private String messageHeader;

    @Column(name = "messagedate", nullable = false)
    private Instant messageDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact idContact) {
        this.contact = idContact;
    }

    public String getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(String messageHeader) {
        this.messageHeader = messageHeader;
    }

    public Instant getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Instant messageDate) {
        this.messageDate = messageDate;
    }

}