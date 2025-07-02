package com.bzu.smartvax.service.dto;

// تأكد من وجود الاستيرادات اللازمة إذا كنت تستخدم أنواع بيانات معينة
// import java.time.LocalDate; // إذا كان لديك حقول LocalDate

public class ParentRegistrationDTO {

    // بيانات الطفل
    public String childId;
    public String childName;
    public String childDob;
    public String childPhone;

    // بيانات الأب
    public String parentName;
    public String parentDob;
    public String parentPhone;
    public String parentEmail; // ✅ تم إضافة حقل البريد الإلكتروني هنا

    // بيانات الحساب
    public String username;
    public String password;

    // ✅ يمكن إضافة Getters و Setters هنا يدوياً أو باستخدام Lombok
    // إذا كنت تستخدم Lombok، قم بإضافة @Getter و @Setter في أعلى الكلاس
    // مثال بدون Lombok (إذا كنت بحاجة لها):

    /*
    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildDob() {
        return childDob;
    }

    public void setChildDob(String childDob) {
        this.childDob = childDob;
    }

    public String getChildPhone() {
        return childPhone;
    }

    public void setChildPhone(String childPhone) {
        this.childPhone = childPhone;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentN
    }

    public String getParentDob() {
        return parentDob;
    }

    public void setParentDob(String parentDob) {
        this.parentDob = parentDob;
    }

    public String getParentPhone() {
        return parentPhone;
    }

    public void setParentPhone(String parentPhone) {
        this.parentPhone = parentPhone;
    }

    public String getParentEmail() { // Getter لـ parentEmail
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) { // Setter لـ parentEmail
        this.parentEmail = parentEmail;
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
    */
}
