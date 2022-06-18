package com.tables.entities;

import javax.persistence.*;

@Entity
@Table(name = "contact", indexes = {
        @Index(name = "contact_username_key", columnList = "username", unique = true)
})
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcontact", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 250)
    private String username;

    @Column(name = "firstname", length = 250)
    private String firstName;

    @Column(name = "familyname", length = 250)
    private String familyName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iduser")
    private Appuser user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public Appuser getUser() {
        return user;
    }

    public void setUser(Appuser idUser) {
        this.user = idUser;
    }

}