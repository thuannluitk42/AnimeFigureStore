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

                for (Integer item: request.getRoleId()) {
                    Roles roles = rolesRepository.findByRoleId(item);
                    setRolesRequest.add(roles);
                }

                users = new Users();
                users.setUsername(request.getEmail());
                users.setPassword(encodePassword(request.getPassword()));
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

                if (StringUtils.hasText(request.getImageName()) && StringUtils.hasText(request.getUrlImage())) {
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
        try {
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


        }catch (Exception e){
            e.printStackTrace();
        }

        return DataResponse.ok(null);

    }

    @Override
    public DataResponse resetRequestPW(String email) {
        logger.info("#resetRequestPW email: {}", email);
        Users u = usersRepository.findUsersByEmailAndIsDelete(email, Boolean.FALSE);

        if (null == u) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

//        String token = jwtTokenHelper.generateJwtTokenWithMail(String.valueOf(System.currentTimeMillis()));
//        logger.info("#token: {}", token);
//        userEntity.setToken(token);
//
//        userRepository.save(userEntity);
//
//        String fullName = userEntity.getFullName();
//        String link = configService.getUrlForgot() + token;
//        String mailTemplate = commonUtilService.getTemplateEmailResetPassword(fullName, link, userEntity.getChannel());
//
//        try {
//            emailSender.sendEmail(userEntity.getEmail(), userEntity.getChannel().name(), SystemConstant.EContractResource.USER_RESET_PW_TITLE, mailTemplate);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//            throw new BadRequestException(ErrorMessage.EMAIL_DOES_NOT_WORK);
//        }
        return DataResponse.ok(null);
    }

}
