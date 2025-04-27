package com.scm.Controllers;

import com.scm.Entities.Contacts;
import com.scm.Services.ContactService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiController {


    private ContactService contactService ;

    @GetMapping("/contacts/{id}")
    public Contacts contactDetails(@PathVariable String id){
        return contactService.getContactById(id) ;
    }

}
