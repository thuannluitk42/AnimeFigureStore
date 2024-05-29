package com.tpsolution.animestore.service.imp;

import com.tpsolution.animestore.entity.Roles;
import com.tpsolution.animestore.payload.DataResponse;

public interface RoleServiceImp {

    DataResponse getAllRole();

    Roles findByRoleId(int roleId);
}
