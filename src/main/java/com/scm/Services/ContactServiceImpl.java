package com.scm.Services;

import com.scm.Entities.Contacts;
import com.scm.Entities.User;
import com.scm.Helpers.UserNotFoundException;
import com.scm.Repositories.ContactRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@AllArgsConstructor
public class ContactServiceImpl implements ContactService{

    private ContactRepository contactRepository ;

    @Override
    public Contacts save(Contacts contacts) {
        String contactId = UUID.randomUUID().toString() ;
        contacts.setId(contactId);
        return contactRepository.save(contacts);
    }

    @Override
    public void delete(String id) {
        var byId = contactRepository.findById(id).orElseThrow(()->
                new UserNotFoundException("Not found"));
         contactRepository.delete(byId);
    }

    @Override
    public Contacts update(Contacts contacts) {

        Contacts old = contactRepository.findById(contacts.getId()).orElseThrow(()->{
            throw new UserNotFoundException("Contact Not found");
        });
        old.setName(contacts.getName());
        old.setEmail(contacts.getEmail());
        old.setPhoneNum(contacts.getPhoneNum());
        old.setDescription(contacts.getDescription());
        old.setAddress(contacts.getAddress());
        old.setFavourite(contacts.isFavourite());
        old.setCloudinaryImagePublicId(contacts.getCloudinaryImagePublicId());
        old.setWebsiteLink(contacts.getWebsiteLink());
        old.setLinkedListLink(contacts.getLinkedListLink());
        old.setProfilePic(contacts.getProfilePic());


        return contactRepository.save(old);
    }

    @Override
    public List<Contacts> getAllContacts() {
        return contactRepository.findAll();
    }

    @Override
    public Contacts getContactById(String id) {
        return contactRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Contact Not Found"));
    }

    @Override
    public Page<Contacts> searchByName(User user, String keyword ,int pageNo, int size, String sortBy, String direction) {

        Sort sort =  direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNo, size, sort) ;

        return contactRepository.findByUserAndNameContaining(user,keyword,pageable);
    }

    @Override
    public Page<Contacts> searchByEmail(User user, String keyword, int pageNo, int size, String sortBy, String direction) {

       Sort sort =  direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

       Pageable pageable = PageRequest.of(pageNo, size, sort) ;

        return contactRepository.findByUserAndEmailContaining(user,keyword,pageable);
    }

    @Override
    public Page<Contacts> searchByPhoneNumber(User user, String keyword, int pageNo, int size, String sortBy, String direction) {
        Sort sort =  direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(pageNo, size, sort) ;

        return contactRepository.findByUserAndPhoneNumContaining(user,keyword,pageable);
    }



    @Override
    public Page<Contacts> getContactsByUserName(User user, int pageNo, int size, String sortBy, String direction) {

        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending() ;

        Pageable pageable = PageRequest.of(pageNo,size,sort) ;

        return contactRepository.findByUser(user,pageable);

    }
}

