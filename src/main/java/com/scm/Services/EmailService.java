package com.scm.Services;

public interface EmailService {

    void sendEmail(String to, String subject, String htmlContent) ;
    void sendEmailForContactUs(String From , String subject, String htmlContent) ;
}
