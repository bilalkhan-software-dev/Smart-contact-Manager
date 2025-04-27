package com.scm.Controllers;

import com.scm.Entities.Contacts;
import com.scm.Entities.User;
import com.scm.Forms.ContactForm;
import com.scm.Forms.ContactSearchForm;
import com.scm.Helpers.AppConstant;
import com.scm.Helpers.Message;
import com.scm.Helpers.MessageType;
import com.scm.Helpers.fetchUserDetail;
import com.scm.Services.ContactService;
import com.scm.Services.ImageService;
import com.scm.Services.UserService;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/user/contacts")
public class ContactController {


    private ContactService contactService ;
    private UserService userService ;
    private ImageService imageService ;

    @RequestMapping("/add")
    public String add(Model model){
        ContactForm contactForm = new ContactForm() ;
//        form.setName("khan");

        model.addAttribute("conForm", contactForm) ;
        log.info("Calling add contact method");
        return "user/add_contact" ;
    }

    @PostMapping("/saveContact")
    public String createAndSaveContactInDb(@Valid @ModelAttribute("conForm") ContactForm contactForm ,BindingResult bindingResult, HttpSession session, Authentication authentication){

        if (bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach((error)-> log.info(error.toString()));
            session.setAttribute("message",   Message.builder()
                                                        .content("Solve the error occurs")
                                                        .messageColorType(MessageType.red)
                                                        .build());
            return "user/add_contact" ;
        }

        String username  = fetchUserDetail.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username) ;

        // image process
        log.info("File is created {}",contactForm.getContactPic().getOriginalFilename());



        Contacts contacts = new Contacts();
        contacts.setName(contactForm.getName());
        contacts.setEmail(contactForm.getEmail());
        contacts.setAddress(contactForm.getAddress());
        contacts.setDescription(contactForm.getDescription());
        contacts.setWebsiteLink(contactForm.getWebsiteLink());
        contacts.setLinkedListLink(contactForm.getLinkedInLink());
        contacts.setPhoneNum(contactForm.getPhoneNum());
        contacts.setUser(user);
        // image url with publicID mean contact id added for future changes
        // **********************************************
        if (contactForm.getContactPic() != null && !contactForm.getContactPic().isEmpty()) {
            // upload image in cloudinary
            log.info("File not empty {}",contactForm.getContactPic());
            String publicId = UUID.randomUUID().toString();
            String fileUrl = imageService.uploadImage(contactForm.getContactPic(), publicId);
            contacts.setProfilePic(fileUrl);              //*
            contacts.setCloudinaryImagePublicId(publicId);//*
        }else {
            log.info("File is empty ");
        }
        // **********************************************

        contacts.setFavourite(contactForm.getFavourite());
        contactService.save(contacts);
        System.out.println(contactForm);
        log.info("Contact saved  Id {}",contactForm);
        session.setAttribute("message",  Message.builder()
                                                   .content("Your contact  have been added Successfully ")
                                                   .messageColorType(MessageType.green)
                                                   .build());
        return "redirect:/user/contacts/add" ;
    }

    @RequestMapping()
    public String viewContact(@RequestParam(value = "page", defaultValue = "0")int page,
                              @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE+"")int size ,
                              @RequestParam(value = "sort", defaultValue = "name")String sortBy,
                              @RequestParam(value = "direction", defaultValue = "asc")String direction,
                              Model model , Authentication authentication
                              ){
        String username  = fetchUserDetail.getEmailOfLoggedInUser(authentication);
        User findingUser = userService.getUserByEmail(username);
        Page<Contacts> contactsByUserName = contactService.getContactsByUserName(findingUser,page,size,sortBy,direction);


        model.addAttribute("pageContact" , contactsByUserName);
        model.addAttribute("contactSearchForm" ,new ContactSearchForm());
        model.addAttribute("pageSize", AppConstant.PAGE_SIZE);
        return "user/view_contact" ;
    }

    @RequestMapping("/search")
    public String search (
            @ModelAttribute ContactSearchForm searchForm ,
            @RequestParam(value = "page", defaultValue = "0")int page ,
            @RequestParam(value = "size", defaultValue = AppConstant.PAGE_SIZE+"") int size ,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy ,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model ,
            Authentication authentication
            ){
        log.info("field {} keyword {}", searchForm.getField(), searchForm.getKeyword());
        String username = fetchUserDetail.getEmailOfLoggedInUser(authentication) ;
        User user = userService.getUserByEmail(username);


        Page<Contacts> pageContactSearch = null ;
        if (searchForm.getField().equalsIgnoreCase("name")) {
            pageContactSearch = contactService.searchByName(user, searchForm.getKeyword(), page, size, sortBy, direction);
        } else if (searchForm.getField().equalsIgnoreCase("email")) {
            pageContactSearch = contactService.searchByEmail(user,searchForm.getKeyword(),page,size,sortBy,direction);
        } else if (searchForm.getField().equalsIgnoreCase("phoneNum")) {
            pageContactSearch = contactService.searchByPhoneNumber(user, searchForm.getKeyword(), page, size, sortBy, direction);
        }

        log.info("Page Contact field {}", pageContactSearch);
        model.addAttribute("pageContact", pageContactSearch);
        model.addAttribute("pageSize" , AppConstant.PAGE_SIZE);
        model.addAttribute("contactSearchForm" ,searchForm);

        return "user/search_contact";
    }

    @RequestMapping("/delete/{id}")
    public String delete (@PathVariable String id, HttpSession session ){
        contactService.delete(id);
        session.setAttribute("message", Message.builder().content("Contact Deleted Successfully !").messageColorType(MessageType.green).build());
        log.info("Contacts  {}  deleted successfully",id);
        return "redirect:/user/contacts" ;
    }


    @RequestMapping("/view/{id}")
    public String updateForm(@PathVariable String id, Model model){

        var contactDetails = contactService.getContactById(id);

        ContactForm contactForm = new ContactForm();

        contactForm.setName(contactDetails.getName());
        contactForm.setEmail(contactDetails.getEmail());
        contactForm.setAddress(contactDetails.getAddress());
        contactForm.setDescription(contactDetails.getDescription());
        contactForm.setPhoneNum(contactDetails.getPhoneNum());
        contactForm.setFavourite(contactDetails.isFavourite());
        contactForm.setWebsiteLink(contactDetails.getWebsiteLink());
        contactForm.setLinkedInLink(contactDetails.getLinkedListLink());
        contactForm.setPicture(contactDetails.getProfilePic());


        model.addAttribute("conForm", contactForm) ;
        model.addAttribute("id" ,id) ;
        return "user/update_contact";
    }


    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    public String update(@PathVariable String id,@Valid @ModelAttribute("conForm") ContactForm contactForm, BindingResult result, HttpSession session){


        if (result.hasErrors()){
            result.getAllErrors().forEach(e ->log.info(e.toString()));
            return "user/update_contact" ;
        }
        Contacts contacts = contactService.getContactById(id) ;
        contacts.setId(id);
        contacts.setName(contactForm.getName());
        contacts.setEmail(contactForm.getEmail());
        contacts.setPhoneNum(contactForm.getPhoneNum());
        contacts.setDescription(contactForm.getDescription());
        contacts.setAddress(contactForm.getAddress());
        contacts.setFavourite(contactForm.getFavourite());



        if (contactForm.getContactPic() != null && !contactForm.getContactPic().isEmpty()) {
            log.info("File is not empty {}",contactForm.getContactPic());
            String publicUid = UUID.randomUUID().toString();
            String url = imageService.uploadImage(contactForm.getContactPic(), publicUid);
            contacts.setProfilePic(url);
            contacts.setCloudinaryImagePublicId(publicUid);
            contactForm.setPicture(url);
        }else {
            log.info("File is empty");
        }

        Contacts updatedContacts = contactService.update(contacts);

        log.info("Contacts Updated {}",updatedContacts);

        session.setAttribute("message", Message
                                                   .builder()
                                                   .content("Contact Updated Successfully !")
                                                   .messageColorType(MessageType.green)
                                                       .build());

        return "redirect:/user/contacts/view/" + id ;
    }
}
