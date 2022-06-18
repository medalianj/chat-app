package com.tables.entities;

import javax.persistence.*;

@Entity
@Table(name = "appuser", indexes = {
        @Index(name = "appuser_unique_username", columnList = "username", unique = true),
        @Index(name = "appuser_email_unique", columnList = "email", unique = true)
}, uniqueConstraints = {
        @UniqueConstraint(name = "appuser_username_unique", columnNames = {"username"})
})
public class Appuser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iduser", nullable = false)
    private Integer id;

    @Column(name = "email", nullable = false, length = 250)
    private String email;

    @Column(name = "passworduser", nullable = false, length = 250)
    private String password;

    @Column(name = "username", nullable = false, length = 250)
    private String username;

    @Column(name = "firstname", length = 250)
    private String firstName;

    @Column(name = "familyname", length = 250)
    private String familyName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setFirstName(String firstname) {
        this.firstName = firstname;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

}