package com.tpsolution.animestore.service.imp;

import jakarta.servlet.http.HttpServletRequest;

public interface LoginServiceImp {
    boolean checkLogin(String username, String password);

    String logout(HttpServletRequest request);

}
