package com.tpsolution.animestore.service.imp;

import com.tpsolution.animestore.entity.Users;
import com.tpsolution.animestore.payload.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface UserServiceImp {

    DataResponse insertNewUser(AddUserRequest request, MultipartFile multipartFile) throws IOException;

    DataResponse updateUser(UpdateUserRequest request);

    DataResponse changePW(ChangePWRequest request);

    DataResponse requestResetPW(String email);

    DataResponse resetPassword(String token, String password, String confirmPassword);

    DataResponse getInfoDetailUser(String userId);

    DataResponse getUserAll(SearchRequest searchRequest);

    DataResponse findAllUser();

    DataResponse changeStatusUser(DeleteIDsRequest request);

    DataResponse changePasswordByUserId(ChangePWIdRequest request);

    Optional<Users> findByEmail(String email);

    void saveUserLoginGG(Users userEntity);
}
