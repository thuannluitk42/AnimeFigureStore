package com.tpsolution.animestore.service;

import com.tpsolution.animestore.dto.UserDTO;
import com.tpsolution.animestore.entity.Users;
import com.tpsolution.animestore.repository.UsersRepository;
import com.tpsolution.animestore.service.imp.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserServiceImp {

    @Autowired
    UsersRepository usersRepository;
    @Override
    public List<UserDTO> getAllUser() {
        List<Users> listUser = usersRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for (Users user : listUser) {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getUserId());
            userDTO.setUserName(user.getUsername());
            userDTO.setPassword(user.getPassword());
            userDTO.setFullName(user.getFullname());
            userDTO.setCreatedDate(user.getCreatedDate());
            userDTOList.add(userDTO);
        }
        return userDTOList;
    }
}
