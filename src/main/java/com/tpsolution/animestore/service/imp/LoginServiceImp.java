package com.tpsolution.animestore.service.imp;

import com.tpsolution.animestore.payload.SignUpRequest;

public interface LoginServiceImp {
    boolean checkLogin(String username, String password);

    boolean addUser(SignUpRequest signUpRequest);

}
