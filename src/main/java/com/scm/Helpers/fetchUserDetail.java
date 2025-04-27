package com.scm.Helpers;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class fetchUserDetail {

    @Value("${server.baseUrl}")
    private  String baseUrl ;

    public static String getEmailOfLoggedInUser(Authentication authentication){

        if (authentication instanceof OAuth2AuthenticationToken auth){

            var clientId = auth.getAuthorizedClientRegistrationId() ;
            var oAuth2User = (OAuth2User)authentication.getPrincipal() ;


            String username = "" ;
            if (clientId.equalsIgnoreCase("google")){
                // Sign in with Google
                  username = Objects.requireNonNull(oAuth2User.getAttribute("email")).toString() ;

            }else if(clientId.equalsIgnoreCase("github")){

                 // sign in with GitHub
//                username = oAuth2User.getAttribute("email");
                username = oAuth2User.getAttribute("email")!= null ?
                        Objects.requireNonNull(oAuth2User.getAttribute("email")).toString() : Objects.requireNonNull(oAuth2User.getAttribute("login")).toString() +"@gmail.com";
            }
            return username ;

        }else {
            System.out.println("Fetching username from database");
            return authentication.getName() ;
        }
    }
    public  String  getVerifyEmail(String emailToken){
        return this.baseUrl + "/auth/verifyEmail?token="+emailToken;
    }
}

