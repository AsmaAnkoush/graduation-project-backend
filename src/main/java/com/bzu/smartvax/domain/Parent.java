package com.bzu.smartvax.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parent")
public class Parent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    private LocalDate dob;

    private String role = "PARENT";

    // ✅ إلزام وجود حساب مستخدم لكل Parent
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private Users user;

    // 🔗 ربط OneToMany مع الأطفال
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Child> children = new ArrayList<>();

    @Column(name = "email")
    private String email;

    // 🟦 Getters و Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    // 🛠️ Builder-style methods
    public Parent id(Long id) {
        this.setId(id);
        return this;
    }

    public Parent name(String name) {
        this.setName(name);
        return this;
    }

    public Parent phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public Parent dob(LocalDate dob) {
        this.setDob(dob);
        return this;
    }

    public Parent role(String role) {
        this.setRole(role);
        return this;
    }

    public Parent user(Users user) {
        this.setUser(user);
        return this;
    }

    public Parent children(List<Child> children) {
        this.setChildren(children);
        return this;
    }
}
