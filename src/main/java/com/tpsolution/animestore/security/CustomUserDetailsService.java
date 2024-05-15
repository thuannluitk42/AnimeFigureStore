package com.tpsolution.animestore.security;

import com.tpsolution.animestore.entity.Users;
import com.tpsolution.animestore.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users userDetail = usersRepository.findByUsername(username);

        if (userDetail == null){
            throw new UsernameNotFoundException("User not found with username: "+ username);
        }
        return new UserInfoDetails(userDetail) ;

    }

}
