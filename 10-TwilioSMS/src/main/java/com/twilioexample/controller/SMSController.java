package com.twilioexample.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilioexample.payload.SmsRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SMSController {

    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.phone_number}")
    private String fromPhoneNumber;

    @PostMapping("/send-sms")
    public ResponseEntity<String> sendSMS(@RequestBody SmsRequest smsRequest) {
        try {
            Twilio.init(accountSid, authToken);
            Message message = Message.creator(
                    new PhoneNumber(smsRequest.getTo()),//to phone number
                    new PhoneNumber(fromPhoneNumber),//from phone number
                    smsRequest.getMessage())
                    .create();
            return ResponseEntity.ok("SMS sent successfully. SID: " + message.getSid());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send SMS: " + e.getMessage());
        }
    }
}