package com.scm.Services;

import com.scm.Entities.User;
import com.scm.Helpers.AppConstant;
import com.scm.Helpers.UserNotFoundException;
import com.scm.Helpers.fetchUserDetail;
import com.scm.Repositories.UserRepository;


import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
public class UserServiceImp implements UserService{


    public PasswordEncoder passwordEncoder;

    public EmailService emailService ;

    public UserRepository userRepos ;
    private fetchUserDetail fetchUserDetail;

    @Override
    public User save(User user) {

        // Random id for every user
        String randomGeneratedUserId = UUID.randomUUID().toString();
        user.setUserId(randomGeneratedUserId);

//        For generating encrypt Password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole_list(List.of(AppConstant.ROLE_USER));


        String token  = UUID.randomUUID().toString() ;
        user.setVerificationCode(token);

        // verification code sent to user's email   '
        User save = userRepos.save(user);
        String url = fetchUserDetail.getVerifyEmail(token) ;
        emailService.sendEmail(user.getEmail(),"Verify Account : Smart Contact Manager",emailVerifyTemplate(url));
        return save;
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepos.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {

        User user1 = new User() ;

        user1.setName(user.getName());
        user1.setEmail(user.getEmail());
        user1.setAbout(user.getAbout());
        user1.setProfilePic(user.getProfilePic());
        user1.setContact(user.getContact());


        User save = userRepos.save(user);
        return Optional.of(save);
    }

    @Override
    public void deleteUser(String id) {
       User user1 = userRepos.findById(id).orElseThrow( ()
               -> new UserNotFoundException("User not found"));
        userRepos.deleteById(id);

    }

    @Override
    public List<User> getAllUser() {
        return userRepos.findAll();
    }


    @Override
    public boolean isUserExistByEmail(String email) {
     return userRepos.existsByEmail(email);
    }

    @Override
    public boolean isUserExist(String name) {
        User user = userRepos.findByName(name).orElse(null);
        return user != null;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepos.findByEmail(email).orElse(null);
    }
    
    
    
    private String emailVerifyTemplate(String url){
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <style>
                        body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }
                        .header { background-color: #1e40af; color: white; padding: 20px; text-align: center; }
                        .content { padding: 20px; background-color: #f8f9fa; border-radius: 5px; margin-top: 20px; }
                        .button { display: inline-block; padding: 10px 20px; background-color: #1e40af; color: white; text-decoration: none; border-radius: 5px; margin: 20px 0; }
                        .footer { text-align: center; margin-top: 20px; font-size: 12px; color: #666; }
                    </style>
                </head>
                <body>
                    <div class="header">
                        <h1>Smart Contact Manager</h1>
                    </div>
                    <div class="content">
                        <h2>Verify Your Account</h2>
                        <p>Thank you for registering with Smart Contact Manager. To complete your registration, please verify your email address by clicking the button below:</p>
                        <div style="text-align: center;">
                            <a href="%s" class="button">Verify Email Address</a>
                        </div>
                        <p>If the button doesn't work, you can copy and paste this link into your browser:</p>
                        <p style="word-break: break-all;">%s</p>
                    </div>
                    <div class="footer">
                        <p>This is an automated message, please do not reply to this email.</p>
                        <p>&copy; 2025 Smart Contact Manager. All rights reserved.</p>
                    </div>
                </body>
                </html>
                """.formatted(url,url) ;   
    }
}
