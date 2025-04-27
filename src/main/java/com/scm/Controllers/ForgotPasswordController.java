package com.scm.Controllers;


import com.scm.Entities.User;
import com.scm.Forms.ForgotPasswordForm;
import com.scm.Helpers.Message;
import com.scm.Helpers.MessageType;
import com.scm.Repositories.UserRepository;
import com.scm.Services.EmailService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.SecureRandom;


@Controller
public class ForgotPasswordController {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Autowired
    public ForgotPasswordController(EmailService emailService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository ;
    }


    private final SecureRandom random = new SecureRandom();

    Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);


    @RequestMapping("/forgotPassword")
    public String viewForgetPage(Model model) {

        model.addAttribute("forgotPassword", new ForgotPasswordForm());
        logger.info("Enter in Forgot Password Controller");
        return "forgotPassword";
    }


    @PostMapping("/sendOtp")
    public String sendOtp(@ModelAttribute ForgotPasswordForm form, Model model, HttpSession session) {
        logger.info("Enter in Forgot OTP Password Controller");
        int randomOTPNumberGenerated = random.nextInt(9000000) + 100000;
        logger.info("OTP Number Generated : {}", randomOTPNumberGenerated);
        String email = form.getEmail();
        User username = userRepository.findByEmail(email).orElse(null);
        if (username != null) {
            logger.info("User Name : {}", username.getEmail());
            String to = username.getEmail();
            String subject = "OTP for reset your password";
            String htmlContent = getHtmlEmailContent(randomOTPNumberGenerated);
            emailService.sendEmail(to, subject, htmlContent);
            logger.info("mail send Successfully");
            session.setAttribute("email", email);
            session.setAttribute("otp", randomOTPNumberGenerated);
            model.addAttribute("forgotPassword", form);
            session.setAttribute("message", Message.builder()
                    .content("We have sent OTP to your Email : " + email + " . Please Enter OTP to reset your password.")
                    .messageColorType(MessageType.green).build());
            logger.info("OTP Number Saved in Session : {}", randomOTPNumberGenerated);
            logger.info("Email Saved in Session : {}", email);
            return "optEntered";
        } else {
            session.setAttribute("message", Message.builder()
                    .content("Your Email is Wrong , Please Entered your registered email.")
                    .messageColorType(MessageType.red)
                    .build());
            logger.info("Email is Wrong");
            return "redirect:/forgotPassword";
        }
    }

    @PostMapping("/verifyOTP")
    public String verifyOTP(HttpSession session, ForgotPasswordForm form, Model model) {
        int otp = (Integer) session.getAttribute("otp");

        if (otp == form.getOTP()) {
            logger.info("OTP is Correct");
            session.setAttribute("message", Message.builder()
                    .content("OTP is Correct")
                    .messageColorType(MessageType.green)
                    .build());
            model.addAttribute("forgotPassword", form);
            return "resetPassword";
        } else {
            session.setAttribute("message", Message.builder()
                    .content("OTP is Wrong")
                    .messageColorType(MessageType.red)
                    .build());
            return "redirect:/forgotPassword";
        }
    }

    @PostMapping("/resetPassword")
    public String changedPassword(HttpSession session, ForgotPasswordForm form, Model model) {

        logger.info("Enter in Reset Password Controller");
        String email = (String) session.getAttribute("email");
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {

            logger.info("Password : {} ", form.getPassword());
            user.setPassword(passwordEncoder.encode(form.getPassword()));
            userRepository.save(user);
//            model.addAttribute("resetPassword", form);
            session.setAttribute("message", Message.builder()
                    .content("Your Password is Changed Successfully")
                    .messageColorType(MessageType.green)
                    .build());
            session.removeAttribute("otp");
            session.removeAttribute("email");
            return "redirect:/login";
        } else {
            session.setAttribute("message", Message.builder()
                    .content("Your Password is not Changed . Something Wrong on Sever")
                    .messageColorType(MessageType.red)
                    .build());
            return "redirect:/forgotPassword";
        }
    }

    private String getHtmlEmailContent(int otp) {
        return """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;">
                    <h2 style="color: #1a365d; text-align: center;">Password Reset Verification</h2>
                    <div style="background-color: #f3f4f6; padding: 20px; border-radius: 8px; margin: 20px 0;">
                        <p style="font-size: 16px; color: #4a5568;">Dear User,</p>
                        <p style="font-size: 16px; color: #4a5568;">You have requested to reset your password. Please use the following OTP to complete your password reset:</p>
                        <h1 style="color: #2563eb; text-align: center; font-size: 32px; margin: 20px 0; letter-spacing: 5px;">%d</h1>
                        <p style="font-size: 14px; color: #64748b;">This OTP will remove when you successfully verified.</p>
                    </div>
                    <p style="font-size: 14px; color: #64748b; text-align: center;">
                        If you did not request this password reset, please contact our support team immediately.
                    </p>
                    <div style="text-align: center; margin-top: 20px; color: #64748b; font-size: 12px;">
                        <p>This is an automated message, please do not reply to this email.</p>
                    </div>
                </div>
                """.formatted(otp);
    }
}
