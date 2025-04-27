package com.scm.Controllers;

import com.scm.Entities.User;
import com.scm.Helpers.Message;
import com.scm.Helpers.MessageType;
import com.scm.Repositories.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class EmailController {



    private final UserRepository userRepository;

    @Autowired
    public EmailController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping("/verifyEmail")
    public String sendEmail(@RequestParam("token") String token, HttpSession session){


        User user = userRepository.findByVerificationCode(token).orElse(null);
        if (user != null){
            System.out.println("Method outside called");
            if (user.getVerificationCode().equals(token)){
                System.out.println("Method inside called");
                user.setEnable(true);
                user.setEmailVerified(true);
                userRepository.save(user);
                session.setAttribute("message", Message.builder().content("Account Successfully Verified ").messageColorType(MessageType.green).build());
                return "success_page";
            }
            session.setAttribute("message", Message.builder().content("Invalid Verification Code ").messageColorType(MessageType.red).build());
            return "error_page";
        }
        session.setAttribute("message", Message.builder().content("Invalid Verification Code ").messageColorType(MessageType.red).build());
        return "error_page";
    }



}
