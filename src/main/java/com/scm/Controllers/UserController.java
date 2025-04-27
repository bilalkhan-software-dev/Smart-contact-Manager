package com.scm.Controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping(value = "/profile")
    public String profile(){

//        Model model,@AuthenticationPrincipal OAuth2User principal) -> optionally for manually passing name into thymeleaf
        logger.info("Enter in User Profile Controller");

        return "user/profile";
    }
}
