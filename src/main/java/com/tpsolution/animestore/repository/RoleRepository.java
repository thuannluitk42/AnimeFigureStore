package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.entity.Roles;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepository extends CrudRepository<Roles, UUID>, JpaSpecificationExecutor<Roles> {
}
