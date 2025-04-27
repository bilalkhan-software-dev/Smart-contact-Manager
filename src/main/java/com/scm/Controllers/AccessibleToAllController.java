package com.scm.Controllers;

import com.scm.Entities.User;
import com.scm.Helpers.fetchUserDetail;
import com.scm.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class AccessibleToAllController {

    private final Logger logger = LoggerFactory.getLogger(AccessibleToAllController.class);
    private final UserService userService;


    @Autowired
    public AccessibleToAllController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {


            if (authentication == null) {
                logger.debug("No authentication present");
                return;
            }


            logger.info("Fetching user info for authenticated user");
            String username = fetchUserDetail.getEmailOfLoggedInUser(authentication);




            logger.info("User logged in: {}", username);
            User userByEmail = userService.getUserByEmail(username);
            if (userByEmail == null) {
                logger.warn("User not found in database: {}", username);
                return;
            }


            logger.debug("Retrieved user: {}", userByEmail);
            logger.info("User Details found for: {}", userByEmail.getName());
            logger.debug("User details - Email: {}, Name: {}", userByEmail.getEmail(), userByEmail.getName());
            model.addAttribute("loggedInUser", userByEmail);


    }
}

