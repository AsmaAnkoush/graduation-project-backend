package com.bzu.smartvax.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsService {

    // Replace with your Twilio credentials
    public static final String ACCOUNT_SID = "YOUR_ACCOUNT_SID";
    public static final String AUTH_TOKEN = "YOUR_AUTH_TOKEN";
    public static final String TWILIO_PHONE_NUMBER = "+1234567890"; // Your Twilio number

    public static void sendVerificationCode(String toPhoneNumber, String code) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
            new PhoneNumber(toPhoneNumber),
            new PhoneNumber(TWILIO_PHONE_NUMBER),
            "Your verification code is: " + code
        ).create();

        System.out.println("Sent message SID: " + message.getSid());
    }

    public static void main(String[] args) {
        String recipient = "+19876543210"; // Example target phone number
        String code = String.valueOf((int) (Math.random() * 900000) + 100000); // Random 6-digit code
        sendVerificationCode(recipient, code);
    }
}
