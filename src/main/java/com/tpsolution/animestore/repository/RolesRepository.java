package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Integer> {
    Roles findByRoleId(Integer roleId);

}