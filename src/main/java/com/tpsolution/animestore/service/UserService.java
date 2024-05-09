package com.tpsolution.animestore.service;

import com.tpsolution.animestore.constant.ErrorMessage;
import com.tpsolution.animestore.entity.Roles;
import com.tpsolution.animestore.entity.Users;
import com.tpsolution.animestore.exception.BadRequestException;
import com.tpsolution.animestore.exception.NotFoundException;
import com.tpsolution.animestore.payload.AddUserRequest;
import com.tpsolution.animestore.payload.ChangePWRequest;
import com.tpsolution.animestore.payload.DataResponse;
import com.tpsolution.animestore.payload.UpdateUserRequest;
import com.tpsolution.animestore.repository.RolesRepository;
import com.tpsolution.animestore.repository.UsersRepository;
import com.tpsolution.animestore.service.imp.UserServiceImp;
import com.tpsolution.animestore.utils.CommonUtils;
import com.tpsolution.animestore.utils.JwtUtilsHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements UserServiceImp {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    JwtUtilsHelper jwtTokenHelper;

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    @Transactional
    public DataResponse insertNewUser(AddUserRequest request) {
        logger.info("#insertNewUser email: {}", request.getEmail());
        Users users;
        Set<Roles> setRolesRequest = new HashSet<>();
        try {
            if (StringUtils.hasText(request.getEmail()) == true && !CommonUtils.checkEmail(request.getEmail())) {
                throw new BadRequestException(ErrorMessage.EMAIL_IS_INVALID);
            }

            if (StringUtils.hasText(request.getEmail()) == false) {
                throw new BadRequestException(ErrorMessage.EMAIL_IS_INVALID);
            }

            users = usersRepository.findUsersByEmailAndIsDelete(request.getEmail(), Boolean.FALSE);

            if (users == null) {

                users = new Users();

                for (Integer item: request.getRoleId()) {
                    Roles roles = rolesRepository.findByRoleId(item);
                    setRolesRequest.add(roles);
                }

                users.setUsername(CommonUtils.extractUsernameFromEmail(request.getEmail()));
                users.setPassword(encodePassword(request.getPassword()));
                users.setEmail(request.getEmail());

                // cac gia tri duoc khoi tao mac dinh khi user dc tao lan dau tien
                users.setFullname(request.getEmail());
                users.setAddress("Nha trang, khánh hòa");
                users.setPhonenumber("0983172229");
                users.setDob("23/03/1990");
                users.setAvatar("");

                users.setDeleted(false);
                users.setRoles(setRolesRequest);
                usersRepository.save(users);
            } else {
                throw new BadRequestException(ErrorMessage.USER_IS_EXISTED);
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return DataResponse.ok(null);
    }

    @Override
    @Transactional
    public DataResponse updateUser(UpdateUserRequest request) {
        logger.info("#updateUser email: {}", request.getEmail());
        Users users;
        Set<Roles> setRolesRequest = new HashSet<>();
        try {
            if (StringUtils.hasText(request.getEmail()) == true && !CommonUtils.checkEmail(request.getEmail())) {
                throw new BadRequestException(ErrorMessage.EMAIL_IS_INVALID);
            }

            if (StringUtils.hasText(request.getEmail()) == false) {
                throw new BadRequestException(ErrorMessage.EMAIL_IS_INVALID);
            }

            users = usersRepository.findUsersByEmailAndIsDelete(request.getEmail(), Boolean.FALSE);

            if (users == null) {
                throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
            } else {

                for (Integer item: request.getRoleId()) {
                    Roles roles = rolesRepository.findByRoleId(item);
                    setRolesRequest.add(roles);
                }

                if (StringUtils.hasText(request.getFullName())) {
                    users.setFullname(request.getFullName());
                } else {
                    users.setFullname(users.getFullname());
                }

                if (StringUtils.hasText(request.getAddress())) {
                    users.setAddress(request.getAddress());
                } else {
                    users.setAddress(users.getAddress());
                }

                if (StringUtils.hasText(request.getPhoneNumber()) && !CommonUtils.checkPhoneNumberVietNam(request.getPhoneNumber())) {
                    throw new BadRequestException(ErrorMessage.PHONE_NUMBER_IS_INVALID);
                }

                if (StringUtils.hasText(request.getPhoneNumber())) {
                    users.setPhonenumber(request.getPhoneNumber());
                } else {
                    users.setPhonenumber(users.getPhonenumber());
                }

                if (StringUtils.hasText(request.getDob()) && !CommonUtils.checkdayOfBirth(request.getDob())) {
                    throw new BadRequestException(ErrorMessage.DAY_OF_BIRTH_IS_INVALID);
                }

                if (StringUtils.hasText(request.getDob())) {
                    users.setDob(request.getDob());
                } else {
                    users.setDob(users.getDob());
                }

                if (StringUtils.hasText(request.getPassword())) {
                    users.setPassword(passwordEncoder.encode(request.getPassword()));
                } else {
                    users.setPassword(users.getPassword());
                }

                if (StringUtils.hasText(request.getUrlImage())) {
                    users.setAvatar(request.getUrlImage());
                } else {
                    users.setAvatar(users.getAvatar());
                }

                users.setDeleted(false);
                users.setRoles(setRolesRequest);

                usersRepository.save(users);

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return DataResponse.ok(null);
    }

    @Override
    @Transactional
    public DataResponse changePW(ChangePWRequest request) {
        logger.info("#changePW email: {}", request.getEmail());
            if (StringUtils.hasText(request.getEmail()) == true && !CommonUtils.checkEmail(request.getEmail())) {
                throw new BadRequestException(ErrorMessage.EMAIL_IS_INVALID);
            }

            if (StringUtils.hasText(request.getEmail()) == false) {
                throw new BadRequestException(ErrorMessage.EMAIL_IS_INVALID);
            }

            Users users = usersRepository.findUsersByEmailAndIsDelete(request.getEmail(), Boolean.FALSE);

            if (users == null) {
                throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);

            } else {

                if (!passwordEncoder.matches(request.getOldPassword(), users.getPassword())) {
                    throw new BadRequestException(ErrorMessage.OLD_PW_ERROR);
                }

                if (request.getNewPassword().equals(request.getOldPassword())) {
                    throw new BadRequestException(ErrorMessage.NEW_PW_DUPLICATED_OLD_PW);
                }
                users.setPassword(passwordEncoder.encode(request.getNewPassword()));
                usersRepository.save(users);
            }

        return DataResponse.ok(null);
    }

    @Override
    public DataResponse requestResetPW(String email) {
        logger.info("#resetRequestPW email: {}", email);
        Users userEntity = usersRepository.findUsersByEmailAndIsDelete(email, Boolean.FALSE);

        if (null == userEntity) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        String token = jwtTokenHelper.generateToken(userEntity.getUsername());
        logger.info("#token: {}", token);
        userEntity.setToken(token);

        usersRepository.save(userEntity);

        return DataResponse.ok(null);
    }

    @Override
    public DataResponse resetPassword(String token, String password, String confirmPassword) {
        logger.info("#resetPassword");
        Users userEntity = usersRepository.findByPasswordResetToken(token);
        if (userEntity == null) {
            throw new NotFoundException(ErrorMessage.TOKEN_NOT_FOUND);
        }

        if (!password.equals(confirmPassword)) {
            throw new NotFoundException(ErrorMessage.COMPARE_PASSWORD_ERROR);
        }

        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity.setToken(null);
        usersRepository.save(userEntity);
        return DataResponse.ok(null);
    }

}
