package com.scm.Entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


import jakarta.persistence.*;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User  implements UserDetails {


    @Id
    private String userId ;

    @Column(nullable = false)
    private String name ;

    @Column(nullable = false , unique = true)
    private String email ;

    @Getter(AccessLevel.NONE)
    private String password ;

    private String phoneNum ;

    @Column(length=1000)
    private String profilePic ;


    @Column (length = 1000)
    private String about ;

    @Getter(AccessLevel.NONE)
    private boolean enable = false;

    private boolean emailVerified = false  ;
    private boolean phoneVerified = false ;


    @Enumerated(value = EnumType.STRING)
    private Providers provider = Providers.SELF ;
    private  String providerUserId  ;


    @OneToMany(mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private List<Contacts> contact = new ArrayList<>();


    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> role_list = new ArrayList<>() ;

    private String verificationCode ;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role_list.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return this.email;
    }
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enable;
    }
}
