package com.tpsolution.animestore.service.imp;

import com.tpsolution.animestore.payload.*;

public interface UserServiceImp {

    DataResponse insertNewUser(AddUserRequest request);

    DataResponse updateUser(UpdateUserRequest request);

    DataResponse changePW(ChangePWRequest request);

    DataResponse requestResetPW(String email);

    DataResponse resetPassword(String token, String password, String confirmPassword);

    DataResponse getInfoDetailUser(String userId);

    DataResponse getUserAll(UsersRequest usersRequest);
}
