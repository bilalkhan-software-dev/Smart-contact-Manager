package com.scm.Helpers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component("sessionHelper")
public class SessionHelper {

    public static void removeMessage (){
        try {
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            session.removeAttribute("message");
        } catch (Exception e) {
            System.out.println("Error in session Helper is : " + e);
            e.printStackTrace();
        }
    }
}
