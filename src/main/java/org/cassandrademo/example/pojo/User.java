package org.cassandrademo.example.pojo;

import me.prettyprint.hom.annotations.Column;
import me.prettyprint.hom.annotations.Id;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author: legezadm
 * @since: 21.07.13
 */
@Entity
@Table(name = "User")
public class User {

    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    /*
        needed for EntityManager
     */
    public User() {
    }

    public User(String id, String name, String surname, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
