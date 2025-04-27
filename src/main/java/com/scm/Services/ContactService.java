package com.scm.Services;

import com.scm.Entities.Contacts;
import com.scm.Entities.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ContactService {

    Contacts save(Contacts contacts);

    Contacts update(Contacts contacts) ;

    List<Contacts> getAllContacts() ;

    Contacts getContactById(String id);

    Page<Contacts> getContactsByUserName(User user, int pageNo,int size, String sortBy, String direction) ;



    void delete(String id);


    Page<Contacts> searchByName(User user,String keyword , int pageNo ,int size, String sortBy, String direction) ;
    Page<Contacts> searchByEmail(User user,String keyword , int pageNo ,int size, String sortBy, String direction) ;
    Page<Contacts> searchByPhoneNumber(User user,String keyword , int pageNo ,int size, String sortBy, String direction) ;





}
