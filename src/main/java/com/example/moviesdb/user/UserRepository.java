package com.example.moviesdb.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    User findByToken(String token);

    @Modifying
    @Query(value = "UPDATE User u SET u.verified = true WHERE u.token = ?1")
    int setVerified(String token);
}
