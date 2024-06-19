package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.entity.Users;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
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

    @Query("SELECT u from Users u  where u.userId =:userId and u.isDeleted =:isDelete")
    Users findUsersByIdAndIsDelete(String userId, Boolean isDelete);

    Optional<Users> findByEmail(String email);

    @Query("SELECT COUNT(u) FROM Users u WHERE u.isLogged = true")
    long countActiveUsers();

}

