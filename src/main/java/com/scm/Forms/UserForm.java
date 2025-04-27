package com.scm.Forms;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserForm {


    @NotBlank(message = "Username is required")
    private  String name ;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email cannot be null")
    private String email ;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Minimum 6 characters is required")
    private String password ;

    @NotBlank(message = "About is required")
    private String about ;

    @NotBlank(message = "Contact number is required")
    @Size(min = 11, message = "Minimum 11 numbers is required")
    private String phoneNum ;
}
