package com.tpsolution.animestore.service;

import com.tpsolution.animestore.constant.ErrorMessage;
import com.tpsolution.animestore.enums.Provider;
import com.tpsolution.animestore.entity.Roles;
import com.tpsolution.animestore.entity.Users;
import com.tpsolution.animestore.exception.BadRequestException;
import com.tpsolution.animestore.payload.AddUserRequest;
import com.tpsolution.animestore.payload.DataResponse;
import com.tpsolution.animestore.repository.RolesRepository;
import com.tpsolution.animestore.repository.UsersRepository;
import com.tpsolution.animestore.security.JwtUtilsHelper;
import com.tpsolution.animestore.service.imp.ClientServiceImp;
import com.tpsolution.animestore.utils.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
@Service
public class ClientService implements ClientServiceImp {

    private static final Logger logger = LogManager.getLogger(ClientService.class);

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private JwtUtilsHelper jwtTokenHelper;
    @Override
    @Transactional
    public DataResponse insertNewClient(AddUserRequest request) {
        logger.info("#insertNewClient email: {}", request.getEmail());

        Set<Roles> setRolesRequest = new HashSet<>();
        if (StringUtils.hasText(request.getEmail()) == true && !CommonUtils.checkEmail(request.getEmail())) {
            throw new BadRequestException(ErrorMessage.EMAIL_IS_INVALID);
        }

        if (StringUtils.hasText(request.getEmail()) == false) {
            throw new BadRequestException(ErrorMessage.EMAIL_IS_INVALID);
        }

        Users users = usersRepository.findUsersByEmailAndIsDelete(request.getEmail(), Boolean.FALSE);

        if (users == null) {

            users = new Users();

            for (Integer item: request.getRoleId()) {
                Roles roles = rolesRepository.findByRoleId(item);
                setRolesRequest.add(roles);
            }

            users.setUsername(CommonUtils.extractUsernameFromEmail(request.getEmail()));
            users.setPassword(request.getPassword());
            users.setEmail(request.getEmail());

            buildClientDefault(request, users, setRolesRequest);

            users = usersRepository.save(users);
        } else {
            throw new BadRequestException(ErrorMessage.USER_IS_EXISTED);
        }

        return DataResponse.ok(users);
    }

    private Users buildClientDefault(AddUserRequest request, Users users, Set<Roles> setRolesRequest){
        // cac gia tri duoc khoi tao mac dinh khi user dc tao lan dau tien
        users.setFullname(CommonUtils.extractUsernameFromEmail(request.getEmail()));
        users.setAddress("Nha trang, khánh hòa");
        users.setPhonenumber("0983172229");
        users.setDob("01/01/1990");
        users.setAvatar("default_avatar.jpg");

        users.setDeleted(false);
        users.setRoles(setRolesRequest);
        String token = jwtTokenHelper.generateToken(CommonUtils.extractUsernameFromEmail(request.getEmail()));
        users.setToken(token);

        users.setProvider(Provider.GOOGLE);

        Date date = new Date();
        users.setCreatedDate(date);
        users.setLogged(Boolean.TRUE);
        return users;
    }


}
