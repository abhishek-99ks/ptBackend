package com.cerner.patienttransfer.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Represents the User entity class
 */
@Entity
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;

    /**
     * Gets the id
     * @return Returns id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id
     * @param id Represents user id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of user
     * @return Returns the name of user
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of user
     * @param name Represents the name of user
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the username of user
     * @return Returns the name of user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of user
     * @param username Represents the username of user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email of user
     * @return Returns the email of user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of user
     * @param email Represents the email of user
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password of a user
     * @return Returns the password of the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of a user
     * @param password Represents the password of user
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
