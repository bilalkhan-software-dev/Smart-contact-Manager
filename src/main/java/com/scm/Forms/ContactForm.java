package com.scm.Forms;


import com.scm.CustomValidation.ImageValidation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactForm {


    @NotBlank(message = "Contact name is required ")
    private String name ;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    private String email ;


    @Pattern(regexp = "^[0-9]{11}$", message = "Invalid Phone Number")
    private String phoneNum ;

    @NotBlank(message = "Address is required")
    @Size(max = 1000,message = "You can entered just 1000 Characters")
    private String address ;


    private String description ;

    private String websiteLink ;
    private String linkedInLink ;

    private boolean favourite = false ;

    @ImageValidation(message = "Invalid file")
    private MultipartFile contactPic ;

    private String picture ;

    public boolean getFavourite() {
        return this.favourite;
    }
}
