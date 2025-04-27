package com.scm.Services;

import com.scm.Entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User save (User user) ;

    // all below is for admin

    Optional<User> getUserById (String id) ;

    Optional<User> updateUser (User user) ;

    void deleteUser(String id) ;  // String is bcz we take user id as a String in User Entities

    List<User> getAllUser();

    boolean isUserExistByEmail (String email) ;

    boolean isUserExist(String name) ;

     User getUserByEmail (String email) ;






}
