package com.bzu.smartvax.service.dto;

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

    // بيانات الحساب
    public String username;
    public String password;
    // ✅ يمكن إضافة Getters و Setters إذا أردت استخدام Lombok أو التعامل معه بشكل تقليدي
}
