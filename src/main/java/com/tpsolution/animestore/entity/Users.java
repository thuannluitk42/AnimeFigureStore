package com.tpsolution.animestore.entity;

import com.tpsolution.animestore.enums.Provider;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Builder
public class Users implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", length = 11, nullable = false, unique = true)
    private int userId;

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "fullname", length = 255, nullable = true)
    private String fullname;

    @Column(name = "address", length = 255, nullable = true)
    private String address;

    @Column(name = "phonenumber", length = 12, nullable = true)
    private String phonenumber;

    @Column(name = "email", length = 255, nullable = true)
    private String email;

    @Column(name = "dob", length = 8, nullable = true)
    private String dob;

    @Column(name = "avatar",nullable = true)
    private String avatar;

    @Column(name = "is_deleted",nullable = true)
    private boolean isDeleted;

    @Column(name = "is_logged",nullable = true)
    private boolean isLogged;

    @Column(name = "created_date",nullable = true)
    private Date createdDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider")
    private Provider provider;

    @Column(name = "oauth2_id",nullable = true)
    private String oauth2Id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Roles> roles = new HashSet<>();

    @Column(name = "token", length = 255, nullable = true)
    private String token;

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }

    public void addRole(Roles role) {
        this.roles.add(role);
    }

    public boolean hasRole(String roleName) {
        Iterator<Roles> iterator = roles.iterator();

        while (iterator.hasNext()) {
            Roles role = iterator.next();
            if (role.getRoleName().equals(roleName)) {
                return true;
            }
        }

        return false;
    }

    public String getRoleName() {
        Iterator<Roles> iterator = roles.iterator();
        String roleName="";
        while (iterator.hasNext()) {
            Roles role = iterator.next();
            roleName =  role.getRoleName();
        }
        return roleName;
    }

    @Transient
    public String getPhotosImagePath() {
        if (null  == avatar) {
            return "/user-photos/default_avatar.jpg";
        } else {
            return "/user-photos/" + userId + "/" + avatar;
        }
    }

//    public Users update(OAuth2UserInfo oAuth2UserInfo) {
//        this.username = oAuth2UserInfo.getName();
//        this.oauth2Id = oAuth2UserInfo.getOAuth2Id();
//        return this;
//    }

}
