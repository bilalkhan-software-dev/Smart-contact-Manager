package com.scm.Repositories;

import com.scm.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {


     Optional<User> findByEmail(String email);

     Optional<User> findByName(String name);
     Optional<User> findByVerificationCode(String token);

     boolean existsByEmail(String email);


}

