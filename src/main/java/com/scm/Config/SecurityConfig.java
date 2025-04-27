package com.scm.Config;

import com.scm.Helpers.Message;
import com.scm.Helpers.MessageType;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private CustomUserDetailService customUserDetailService ;
    private OAuthSuccessHandler handler ;


    @Bean
    public PasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder() ;
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider (){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider() ;
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        provider.setUserDetailsService(customUserDetailService);
        return  provider ;
    }


//    Main config that control our page in browser

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer :: disable)
                .authorizeHttpRequests(authorize
                        ->
                        authorize.requestMatchers("/user/**")
                                .authenticated()
                                .anyRequest()
                                .permitAll()
                ).formLogin(formLogin
                        -> formLogin.loginPage("/login")
                                .loginProcessingUrl("/do-login")
                                .defaultSuccessUrl("/user/profile")
                                .failureForwardUrl("/login?error=true")
                                .usernameParameter("email").passwordParameter("password").failureHandler((request,response,exception)->{

                                    if (exception instanceof DisabledException) {
                                        HttpSession session = request.getSession();
                                        session.setAttribute("message", Message.builder().content("User is disable. Email with  verification link is sent on your email").messageColorType(MessageType.red).build());
                                        response.sendRedirect("/login");
                                    }else {
                                        response.sendRedirect("/login?error=true");
                                    }
                        })
                ).logout(logout
                         -> logout.logoutUrl("/do-logout")
                                .logoutSuccessUrl("/login?logout=true")
                        )
//        oAuth Configure
                .oauth2Login(oAuthLogin
                        -> oAuthLogin
                                .loginPage("/login")
                                .successHandler(handler)
                );
        return  http.build();
    }

}
