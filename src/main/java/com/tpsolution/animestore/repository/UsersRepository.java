package com.tpsolution.animestore.repository;

import com.tpsolution.animestore.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    List<Users> findUsersByUsernameAndPassword(String username, String password);
    Users findByUsername(String username);

}

