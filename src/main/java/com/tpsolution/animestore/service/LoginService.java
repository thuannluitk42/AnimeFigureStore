package com.tpsolution.animestore.service;

import com.tpsolution.animestore.entity.Roles;
import com.tpsolution.animestore.entity.Users;
import com.tpsolution.animestore.payload.SignUpRequest;
import com.tpsolution.animestore.repository.RolesRepository;
import com.tpsolution.animestore.repository.UsersRepository;
import com.tpsolution.animestore.service.imp.LoginServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class LoginService implements LoginServiceImp {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public boolean checkLogin(String username, String password){
        Users user = usersRepository.findByUsername(username);
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public boolean addUser(SignUpRequest signUpRequest) {
        Users users = new Users();

        Roles roles = new Roles();
        Set<Roles> setRolesRequest = new HashSet<>();

        for (Integer item: signUpRequest.getRoleId()) {
            roles = rolesRepository.findByRoleId(item);
            setRolesRequest.add(roles);
        }

        users.setFullname(signUpRequest.getFullName());
        users.setUsername(signUpRequest.getEmail());
        users.setPassword(signUpRequest.getPassword());
        users.setRoles(setRolesRequest);
        try {
            usersRepository.save(users);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
