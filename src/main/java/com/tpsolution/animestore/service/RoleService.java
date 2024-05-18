package com.tpsolution.animestore.service;

import com.tpsolution.animestore.entity.Roles;
import com.tpsolution.animestore.payload.DataResponse;
import com.tpsolution.animestore.payload.RoleDetailResponse;
import com.tpsolution.animestore.repository.RoleRepository;
import com.tpsolution.animestore.service.imp.RoleServiceImp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService implements RoleServiceImp {

    private static final Logger logger = LogManager.getLogger(RoleService.class);

    @Autowired
    private RoleRepository rolesRepository;

    @Override
    public DataResponse getAllRole() {
        logger.info("getAllRole");

        List<RoleDetailResponse> list = new ArrayList<>();
        for (Roles item: rolesRepository.findAll()) {
            RoleDetailResponse data = build(item);
            list.add(data);
        }

        return DataResponse.ok(list);
    }

    public RoleDetailResponse build(Roles roles) {
        RoleDetailResponse data = new RoleDetailResponse();
        data.setRoleId(roles.getRoleId());
        data.setRoleName(roles.getRoleName());
        return data;
    }
}
