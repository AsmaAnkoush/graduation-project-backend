package com.bzu.smartvax.domain;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class Users implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    // 🔁 ربط عكسي مع Parent (اختياري فقط إذا أردت التنقل من المستخدم إلى الأب)
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Parent parent;

    @Column(name = "reference_id")
    private Long referenceId;

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    // Builder-style methods
    public Users id(Long id) {
        this.id = id;
        return this;
    }

    public Users username(String username) {
        this.username = username;
        return this;
    }

    public Users password(String password) {
        this.password = password;
        return this;
    }

    public Users role(String role) {
        this.role = role;
        return this;
    }

    public Users parent(Parent parent) {
        this.parent = parent;
        return this;
    }
}
