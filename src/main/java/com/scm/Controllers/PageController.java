package com.scm.Controllers;


import com.scm.Entities.User;
import com.scm.Forms.UserForm;
import com.scm.Helpers.Message;
import com.scm.Helpers.MessageType;
import com.scm.Services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@AllArgsConstructor
@Controller
public class PageController {


    private UserService userService;


    @RequestMapping("/home")
    public String home() {
        return "home";
    }

    @RequestMapping("/about")
    public String about() {
        return "about";
    }

    @RequestMapping("/services")
    public String services() {
        return "services";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/register")
    public String register(Model model) {

        //    For Manually -> Default sending value to the register
        UserForm userForm = new UserForm();

        // sending this object to the user for save signup user in userFrom Class
        model.addAttribute("userForm", userForm);
        return "register";
    }


    @PostMapping("/createUser")
    public String userSaveInDb(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "register";
        }
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setAbout(userForm.getAbout());
        user.setPassword(userForm.getPassword());
        user.setPhoneNum(userForm.getPhoneNum());
        user.setProfilePic(
                """
                        https://www.google.com/imgres?q=default%20profile%20picture%5C&imgurl=https%3A%2F%2Fstatic.vecteezy.com%2Fsystem%2Fresources%2Fthumbnails%2F020%2F765%2F399%2Fsmall_2x%2Fdefault-profile-account-unknown-icon-black-silhouette-free-vector.jpg&imgrefurl=https%3A%2F%2Fwww.vecteezy.com%2Ffree-vector%2Fdefault-profile-picture&docid=--oA6_9U9ufzsM&tbnid=bowkO_GA3uhk3M&vet=12ahUKEwjdtrPQl8qMAxWjTqQEHSuZC8YQM3oECGQQAA..i&w=400&h=400&hcb=2&ved=2ahUKEwjdtrPQl8qMAxWjTqQEHSuZC8YQM3oECGQQAA"""
        );
        if (userService.isUserExistByEmail(userForm.getEmail())) {
            session.setAttribute("message", Message.builder().content("The email you entered is already registered").messageColorType(MessageType.red).build());
            return "redirect:/register";
        }

        userService.save(user);
        Message message = Message.builder().content("Registration Successfully").messageColorType(MessageType.green).build();
        session.setAttribute("message", message);

        System.out.println("User Saved " + "\n" + user);
        return "redirect:/register";
    }
}
