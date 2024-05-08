package com.tpsolution.animestore.service;

import com.tpsolution.animestore.entity.Roles;
import com.tpsolution.animestore.entity.Users;
import com.tpsolution.animestore.payload.SignUpRequest;
import com.tpsolution.animestore.repository.RolesRepository;
import com.tpsolution.animestore.repository.UsersRepository;
import com.tpsolution.animestore.service.imp.LoginServiceImp;
import jakarta.servlet.http.HttpServletRequest;
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
    public String logout(HttpServletRequest request) {
//        String email = "";
//        String token = "";
//        String tokenHeader = request.getHeader(SystemConstant.HeaderConstant.AUTHORIZATION);
//        if (StringUtils.hasText(tokenHeader) && tokenHeader.startsWith(SystemConstant.HeaderConstant.BEARER)) {
//            token = tokenHeader.substring(7);
//            email = jwtHelper.getUsernameFromToken(token);
//            User user = userRepository.findUserByEmailAndIsDelete(email, Boolean.FALSE).orElseThrow(() -> {
//                throw new NotFoundException(ErrorMessage.ACCOUNT_NOT_FOUND);
//            });
//            user.setIsLogin(Boolean.FALSE);
//            userRepository.save(user);
//        }
//        return ErrorCode.SUCCESS.name();
        return "";
    }

}
