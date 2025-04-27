package com.scm.Controllers;

import com.scm.Forms.GetInTouchForm;
import com.scm.Helpers.Message;
import com.scm.Helpers.MessageType;
import com.scm.Services.EmailService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Slf4j
@Controller
public class GetInTouchController {


    private final EmailService emailService;

    @Autowired
    public GetInTouchController(EmailService emailService) {
        this.emailService = emailService;
    }

    @RequestMapping("/contact")
    public String emailVerify(Model model) {

        //    For Manually -> Default sending value to the register
        GetInTouchForm getInTouchForm = new GetInTouchForm();

        // sending this object to the user for save signup user in userFrom Class
        model.addAttribute("emailForm", getInTouchForm);
        return "contact";
    }

    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public String sendEmail(@ModelAttribute GetInTouchForm getInTouchForm, HttpSession session) {

        sendEmail(getInTouchForm);
        log.info("Email sent successfully");
        session.setAttribute("message", Message.builder().content("Thank you for your message! We'll get back to you soon.").messageColorType(MessageType.green).build());
        return "redirect:/contact";
    }

    public void sendEmail(GetInTouchForm getInTouchForm) {
        try {
            String to = getInTouchForm.getEmail();
            String subject = "Contact Us";
            String htmlContent = getHtmlEmailContentForUser(getInTouchForm);
            String htmlContentForOwner = getHtmlEmailForOwner(getInTouchForm);
            emailService.sendEmail(to, subject, htmlContent);
            emailService.sendEmailForContactUs(to, subject, htmlContentForOwner);
        } catch (Exception e) {
            log.info("{}", String.valueOf(e));
        }
    }

    private String getHtmlEmailContentForUser(GetInTouchForm form) {
        // Read the template file from resources
        try {
            String template = """
                     <!DOCTYPE html>
                     <html lang="en">
                     <head>
                         <meta charset="UTF-8">
                         <meta name="viewport" content="width=device-width, initial-scale=1.0">
                         <title>Contact Response</title>
                         <style>
                             body {
                                 margin: 0;
                                 padding: 0;
                                 font-family: Arial, sans-serif;
                                 line-height: 1.6;
                                 background-color: #f4f4f4;
                             }
                             .container {
                                 max-width: 600px;
                                 margin: 0 auto;
                                 padding: 20px;
                                 background-color: #ffffff;
                             }
                             .header {
                                 background-color: #3b82f6;
                                 color: #ffffff;
                                 padding: 20px;
                                 text-align: center;
                             }
                             .content {
                                 padding: 20px;
                                 color: #333333;
                             }
                             .footer {
                                 background-color: #f8f8f8;
                                 padding: 20px;
                                 text-align: center;
                                 color: #666666;
                                 font-size: 12px;
                             }
                         </style>
                     </head>
                     <body>
                         <div class="container">
                             <div class="header">
                                 <h1>Thank You for Contacting Us</h1>
                             </div>
                             <div class="content">
                                 <p>Dear <span style="font-weight: bold;">%s</span>,</p>\s
                                 <p>Thank you for reaching out to us. We have received your message:</p>
                                \s
                                <br>
                                <br>
                               \s
                                 <p>Our team will review your message and get back to you as soon as possible. We typically respond within 24-48 business hours.</p>
                                \s
                                 <p>Best regards,<br>BK_QA Teams</p>
                             </div>
                             <div class="footer">
                                 <p>This is an automated response. Please do not reply to this email.</p>
                                 <p>© 2025 Smart Contact Manager . All rights reserved.</p>
                             </div>
                         </div>
                     </body>
                     </html>
                    \s""";
            return String.format(template, form.getName());
        } catch (Exception e) {
            log.error("Error creating email content", e);
            return "Error creating email content";
        }
    }

    private String getHtmlEmailForOwner(GetInTouchForm form) {
        // Read the template file from resources
        try {
            String template = """
                     <!DOCTYPE html>
                     <html lang="en">
                     <head>
                         <meta charset="UTF-8">
                         <meta name="viewport" content="width=device-width, initial-scale=1.0">
                         <title>Contact Response</title>
                         <style>
                             body {
                                 margin: 0;
                                 padding: 0;
                                 font-family: Arial, sans-serif;
                                 line-height: 1.6;
                                 background-color: #f4f4f4;
                             }
                             .container {
                                 max-width: 600px;
                                 margin: 0 auto;
                                 padding: 20px;
                                 background-color: #ffffff;
                             }
                             .header {
                                 background-color: #3b82f6;
                                 color: #ffffff;
                                 padding: 20px;
                                 text-align: center;
                             }
                             .content {
                                 padding: 20px;
                                 color: #333333;
                             }
                             .footer {
                                 background-color: #f8f8f8;
                                 padding: 20px;
                                 text-align: center;
                                 color: #666666;
                                 font-size: 12px;
                             }
                         </style>
                     </head>
                     <body>
                         <div class="container">
                             <div class="header">
                                 <h1>User Message </h1>
                             </div>
                             <div class="content">
                                 <p>Name <span style="font-weight: bold;">%s</span>,</p>\s
                                 <p>Email <span style="font-weight: bold;">%s</span>,</p>\s
                                \s
                                <h1 class="content">Message</h1>
                                 <p style="background-color: #f8f8f8; padding: 15px; border-left: 4px solid #3b82f6;">
                                     %s
                                 </p>
                                \s
                                 <p>Best regards,<br>BK_QA Teams</p>
                             </div>
                             <div class="footer">
                                 <p>© 2025 Smart Contact Manager . All rights reserved.</p>
                             </div>
                         </div>
                     </body>
                     </html>
                    \s""";
            return String.format(template, form.getName(), form.getEmail(), form.getMessage());
        } catch (Exception e) {
            log.error("Error creating email content", e);
            return "Error creating email content";
        }
    }
}