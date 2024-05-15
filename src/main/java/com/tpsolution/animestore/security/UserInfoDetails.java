package com.tpsolution.animestore.security;

import com.tpsolution.animestore.entity.Roles;
import com.tpsolution.animestore.entity.Users;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class UserInfoDetails implements UserDetails {

    private String name;
    private String password;
    private List<GrantedAuthority> authorities;

    public UserInfoDetails(Users userInfo) {

        Iterator<Roles> iterator = userInfo.getRoles().iterator();
        List<String> roleName = new ArrayList<>();

        while (iterator.hasNext()) {
            Roles role = iterator.next();
            roleName.add(role.getRoleName());
        }

        name = userInfo.getUsername();
        password = userInfo.getPassword();

        authorities = new ArrayList<>();

        for (String item:roleName) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(item);
            authorities.add(simpleGrantedAuthority);
        }

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
}
