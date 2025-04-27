package com.scm.Repositories;

import com.scm.Entities.Contacts;
import com.scm.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contacts, String> {

    // find contact by user
    Page<Contacts> findByUser(User user, Pageable pageable);



    // custom query #optional
    @Query("SELECT c FROM Contacts c where c.user.userId = :userId")
    List<Contacts> findByUserId(@Param("userId") String userId) ;

    // Search query
    Page<Contacts> findByUserAndNameContaining(User user,String nameKeyword,Pageable pageable) ;
    Page<Contacts> findByUserAndEmailContaining(User user,String emailKeyword,Pageable pageable) ;
    Page<Contacts> findByUserAndPhoneNumContaining(User user,String phoneNumKeyword,Pageable pageable) ;





}
