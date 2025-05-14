package com.bzu.smartvax.service.dto;

public class ChildValidationDTO {

    private String id; // رقم الهوية
    private String name; // اسم الطفل
    private String dob; // تاريخ الميلاد (بصيغة yyyy-MM-dd من React)
    private String phone; // رقم هاتف الأب أو ولي الأمر

    // --- Getters and Setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
