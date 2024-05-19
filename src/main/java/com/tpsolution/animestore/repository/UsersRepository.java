package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.entity.Users;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UsersRepository extends CrudRepository<Users, UUID>, JpaSpecificationExecutor<Users> {
    List<Users> findUsersByUsernameAndPassword(String username, String password);
    Users findByUsername(String username);

    @Query("SELECT u from Users u  where (lower(u.email) like lower(:email)) and u.isDeleted =:isDelete")
    Users findUsersByEmailAndIsDelete(String email, Boolean isDelete);

    @Query("SELECT u from Users u  where (lower(u.token) like lower(:token))")
    Users findByPasswordResetToken(String token);

    Users getUsersByUserId(int userId);

    Optional<Users> findByEmail(String email);
}

