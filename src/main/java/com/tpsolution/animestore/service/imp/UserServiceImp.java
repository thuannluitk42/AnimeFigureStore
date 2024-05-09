package com.tpsolution.animestore.service.imp;

import com.tpsolution.animestore.payload.AddUserRequest;
import com.tpsolution.animestore.payload.ChangePWRequest;
import com.tpsolution.animestore.payload.DataResponse;
import com.tpsolution.animestore.payload.UpdateUserRequest;

public interface UserServiceImp {

    DataResponse insertNewUser(AddUserRequest request);

    DataResponse updateUser(UpdateUserRequest request);

    DataResponse changePW(ChangePWRequest request);

    DataResponse requestResetPW(String email);

    DataResponse resetPassword(String token, String password, String confirmPassword);
}
