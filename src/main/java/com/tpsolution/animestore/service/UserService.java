package com.tpsolution.animestore.service;

import com.tpsolution.animestore.constant.ErrorMessage;
import com.tpsolution.animestore.dto.RolesDTO;
import com.tpsolution.animestore.enums.Provider;
import com.tpsolution.animestore.entity.Roles;
import com.tpsolution.animestore.entity.Users;
import com.tpsolution.animestore.exception.BadRequestException;
import com.tpsolution.animestore.exception.NotFoundException;
import com.tpsolution.animestore.payload.*;
import com.tpsolution.animestore.repository.RolesRepository;
import com.tpsolution.animestore.repository.UserCriteriaRepository;
import com.tpsolution.animestore.repository.UsersRepository;
import com.tpsolution.animestore.service.imp.UserServiceImp;
import com.tpsolution.animestore.utils.CommonUtils;
import com.tpsolution.animestore.security.JwtUtilsHelper;
import com.tpsolution.animestore.utils.FileUploadUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.tpsolution.animestore.repository.UserCriteriaRepository.withClientSearch;

@Service
public class UserService implements UserServiceImp {

    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private JwtUtilsHelper jwtTokenHelper;

    @Autowired
    private UserCriteriaRepository userCriteriaRepository;

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    @Transactional
    public DataResponse insertNewUser(AddUserRequest request, MultipartFile multipartFile) throws IOException {
        logger.info("#insertNewUser email: {}", request.getEmail());

        Set<Roles> setRolesRequest = new HashSet<>();
            if (StringUtils.hasText(request.getEmail()) && !CommonUtils.checkEmail(request.getEmail())) {
                throw new BadRequestException(ErrorMessage.EMAIL_IS_INVALID);
            }

            if (!StringUtils.hasText(request.getEmail())) {
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
                users.setPassword(encodePassword(request.getPassword()));
                users.setEmail(request.getEmail());

                if (StringUtils.hasText(request.getUrlImage())) {
                    users.setAvatar(request.getUrlImage());
                } else {
                    users.setAvatar("/user-photos/default_avatar.jpg");
                }

                buildUserDefault(request, users, setRolesRequest);

                users = usersRepository.save(users);

                if (!multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    String uploadDir = "user-photos/"+users.getUserId();
                    FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
                }

            } else {
                throw new BadRequestException(ErrorMessage.USER_IS_EXISTED);
            }
        UserDetailResponse userData = build(users);
        return DataResponse.ok(userData.getUserId());
    }

    @Override
    @Transactional
    public DataResponse updateUser(UpdateUserRequest request) {
        logger.info("#updateUser email: {}", request.getEmail());
        Users users;
        Set<Roles> setRolesRequest = new HashSet<>();
            if (StringUtils.hasText(request.getEmail()) && !CommonUtils.checkEmail(request.getEmail())) {
                throw new BadRequestException(ErrorMessage.EMAIL_IS_INVALID);
            }

            if (!StringUtils.hasText(request.getEmail())) {
                throw new BadRequestException(ErrorMessage.EMAIL_IS_INVALID);
            }

//            users = usersRepository.findUsersByEmailAndIsDelete(request.getEmail(), Boolean.FALSE);
        users = usersRepository.findUsersByIdAndIsDelete(String.valueOf(request.getUserId()), Boolean.FALSE);

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

                if (StringUtils.hasText(request.getUrlImage())) {
                    users.setAvatar(request.getUrlImage());
                } else {
                    users.setAvatar(users.getAvatar());
                }

                users.setRoles(setRolesRequest);

                Date date = new Date();
                users.setCreatedDate(date);

                users = usersRepository.save(users);

            }

        build(users);
        return DataResponse.ok("");
    }

    @Override
    @Transactional
    public DataResponse changePW(ChangePWRequest request) {
        logger.info("#changePW email: {}", request.getEmail());
            if (StringUtils.hasText(request.getEmail()) && !CommonUtils.checkEmail(request.getEmail())) {
                throw new BadRequestException(ErrorMessage.EMAIL_IS_INVALID);
            }

            if (!StringUtils.hasText(request.getEmail())) {
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
                users = usersRepository.save(users);
            }

        return DataResponse.ok(users);
    }

    @Override
    @Transactional
    public DataResponse requestResetPW(String email) {
        logger.info("#resetRequestPW email: {}", email);
        Users userEntity = usersRepository.findUsersByEmailAndIsDelete(email, Boolean.FALSE);

        if (null == userEntity) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        String token = jwtTokenHelper.generateToken(userEntity.getUsername());
        logger.info("#token: {}", token);
        userEntity.setToken(token);
        userEntity.setPassword(encodePassword("Abc12345@"));

        usersRepository.save(userEntity);

        return DataResponse.ok(userEntity);
    }

    @Override
    @Transactional
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
        userEntity = usersRepository.save(userEntity);
        return DataResponse.ok(userEntity);
    }

    @Override
    public DataResponse getInfoDetailUser(String userId) {
        logger.info("#getInfoDetailUser: "+userId);
        Users userEntity = usersRepository.getUsersByUserId(Integer.valueOf(userId));

        if (null == userEntity) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        UserDetailResponse userData = build(userEntity);

        return DataResponse.ok(userData);
    }

    @Override
    @Transactional
    public DataResponse getUserAll(SearchRequest searchRequest) {
        logger.info("#getUserAll");
        if (searchRequest == null) {
            throw new BadRequestException(ErrorMessage.SEARCH_REQUEST_IS_NOT_NULL);
        }
        int page = searchRequest.getPage();
        if (page > 0) {
            page = page - 1;
        }
        Pageable pageableRequest = PageRequest.of(page, searchRequest.getSize(), Sort.by(Sort.Direction.DESC, "createdDate"));
        UserResponse userResponse = new UserResponse();

        Page<Users> usersPage = userCriteriaRepository.findAll(buildSpecification(searchRequest), pageableRequest);
        if (!usersPage.hasContent()) {
            logger.info("Query with empty data");
            userResponse.setList(Collections.emptyList());
            return DataResponse.ok(userResponse);
        }
        userResponse.setList(usersPage.get().map(this::build).collect(Collectors.toList()));
        userResponse.setCurrentPage(searchRequest.getPage());
        userResponse.setTotalPage(usersPage.getTotalPages());
        userResponse.setTotalElement(usersPage.getTotalElements());
        return DataResponse.ok(userResponse);
    }

    @Override
    public DataResponse findAllUser() {
        List<UserDetailResponse> list = new ArrayList<>();
        for (Users userEntity: usersRepository.findAll()) {
            UserDetailResponse userData = build(userEntity);
            list.add(userData);
        }

        return DataResponse.ok(list);
    }

    public UserDetailResponse build(Users users) {
        UserDetailResponse userDetailResponse = new UserDetailResponse();

        userDetailResponse.setUserId(users.getUserId());
        userDetailResponse.setDob(users.getDob());
        userDetailResponse.setAddress(users.getAddress());

        if (StringUtils.hasText(users.getAvatar())) {
            userDetailResponse.setAvatar(users.getPhotosImagePath());
        } else {
            userDetailResponse.setAvatar("/user-photos/default_avatar.jpg");
        }


        userDetailResponse.setEmail(users.getEmail());
        userDetailResponse.setFullName(users.getFullname());
        userDetailResponse.setPhoneNumber(users.getPhonenumber());
        userDetailResponse.setDeleted(users.isDeleted());
        //userDetailResponse.setPassword(users.getPassword());

        Set<Roles> roles = users.getRoles();
        List rolesSet = new ArrayList<>();

        if (!roles.isEmpty()) {

            Iterator<Roles> roleArr = roles.iterator();
            while (roleArr.hasNext()) {
                Roles item = roleArr.next();

                RolesDTO rolesDTO = new RolesDTO();
                rolesDTO.setRoleId(item.getRoleId());
                rolesDTO.setRoleName(item.getRoleName());

                rolesSet.add(rolesDTO);
            }
        }
        userDetailResponse.setRoleList(rolesSet);

        return userDetailResponse;
    }

    private Specification<Users> buildSpecification(SearchRequest searchRequest) {
        Specification<Users> spec = Specification.where(UserCriteriaRepository.withIsDeleted(Boolean.FALSE));

        String search = searchRequest.getSearch();
        if (StringUtils.hasText(search)) {
            spec = spec.and(withClientSearch(search));
        }
        return spec;
    }

    @Override
    @Transactional
    public DataResponse changeStatusUser(DeleteIDsRequest request) {
        logger.info("#changeStatusUser");

        if (request.getList().isEmpty()) {
            return DataResponse.ok("");
        }

        List<Users> usersChange = new ArrayList<>();

        for (Integer item:request.getList()) {

            if (!CommonUtils.isNumeric(String.valueOf(item))) {
                throw new BadRequestException(ErrorMessage.USER_ID_IS_INVALID);
            }

            Users userEntity = usersRepository.getUsersByUserId(item);

            if (null == userEntity) {
                throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
            }

            boolean isStatus = userEntity.isDeleted();

            if (isStatus == Boolean.TRUE) {
                userEntity.setDeleted(Boolean.FALSE);
            } else {
                userEntity.setDeleted(Boolean.TRUE);
            }

            usersChange.add(userEntity);
        }

        Iterable<Users> entities =  usersRepository.saveAll(usersChange);

        return DataResponse.ok(entities);
    }

    @Override
    public DataResponse changePasswordByUserId(ChangePWIdRequest request) {
        logger.info("#changePasswordByUserId");

        Users users = usersRepository.findUsersByIdAndIsDelete(request.getUserId(), Boolean.FALSE);

        if ( null == users) {
            throw new NotFoundException(ErrorMessage.USER_NOT_FOUND);
        } else {
            users.setPassword(passwordEncoder.encode(request.getNewPassword()));
            users = usersRepository.save(users);
        }

        return DataResponse.ok(users);
    }

    @Override
    public Optional<Users> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }


    public void saveUserLoginGG(Users userEntity) {
        logger.info("#saveUserLoginGG", userEntity.getEmail());
        Users users = usersRepository.save(userEntity);
    }


    private Users buildUserDefault(AddUserRequest request, Users users, Set<Roles> setRolesRequest){
        // cac gia tri duoc khoi tao mac dinh khi user dc tao lan dau tien
        users.setFullname(CommonUtils.extractUsernameFromEmail(request.getEmail()));
        if (StringUtils.hasText(request.getAddress())) {
            users.setAddress(request.getAddress());
        } else {
            users.setAddress("Nha trang, khánh hòa");
        }

        if (StringUtils.hasText(request.getPhoneNumber()) && CommonUtils.checkPhoneNumberVietNam(request.getPhoneNumber())) {
            users.setPhonenumber(request.getPhoneNumber());
        } else {
            users.setPhonenumber("0983172229");
        }

        if (StringUtils.hasText(request.getDob()) && CommonUtils.checkdayOfBirth(request.getDob())) {
            users.setDob(request.getDob());
        } else {
            users.setDob("01/01/1990");
        }

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
