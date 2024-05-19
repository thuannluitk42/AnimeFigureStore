package com.tpsolution.animestore.security;

import com.tpsolution.animestore.entity.Roles;
import com.tpsolution.animestore.entity.Users;
import com.tpsolution.animestore.enums.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.util.*;

@Getter
@AllArgsConstructor

public class UserInfoDetails implements OAuth2User, UserDetails {
    private Long id;
    private String name;
    private String password;
    private List<GrantedAuthority> authorities;

    @Setter
    private Map<String, Object> attributes;

    public UserInfoDetails(Users userInfo) {
        name = userInfo.getUsername();
        password = userInfo.getPassword();
        authorities = new ArrayList<>();

        Iterator<Roles> iterator = userInfo.getRoles().iterator();
        List<String> roleName = new ArrayList<>();

        while (iterator.hasNext()) {
            Roles role = iterator.next();
            roleName.add(role.getRoleName());
        }

        for (String item:roleName) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(item);
            authorities.add(simpleGrantedAuthority);
        }

    }

    // userinfo oauth2
    public UserInfoDetails(Long id, String email, List<GrantedAuthority> authorities) {
        this.id = id;
        this.name = email;
        this.authorities = authorities;
    }

    public static UserInfoDetails create(Users user, Map<String, Object> attributes) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(Role.ROLE_CLIENT.name()));
        UserInfoDetails userPrincipal = new UserInfoDetails(Long.valueOf(user.getUserId()), user.getEmail(), authorities);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
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
        return true;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
