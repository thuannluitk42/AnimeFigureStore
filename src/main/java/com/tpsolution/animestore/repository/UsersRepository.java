package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends PagingAndSortingRepository<Users, UUID>, JpaRepository<Users, UUID> {

    List<Users> findUsersByUsernameAndPassword(String username, String password);
    Users findByUsername(String username);

    @Query("SELECT u from Users u  where (lower(u.email) like lower(:email)) and u.isDeleted =:isDelete")
   Users findUsersByEmailAndIsDelete(String email, Boolean isDelete);

}

