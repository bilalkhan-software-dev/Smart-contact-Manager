package com.scm.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Contacts {

    @Id
    private String id ;
    private String name ;
    private String email ;
    private String phoneNum ;

    @Column(length=1000)
    private String description ;
    private String profilePic ;
    private String address ;

    private boolean favourite = false;

    private String websiteLink ;
    private String linkedListLink ;
    private String cloudinaryImagePublicId ;


    @ManyToOne
    @JsonIgnore
    private User user ;


    @OneToMany(mappedBy = "contacts", cascade = CascadeType.ALL , fetch = FetchType.EAGER, orphanRemoval = true)
    private List<SocialLink> socialLinks = new ArrayList<>();




    

}
