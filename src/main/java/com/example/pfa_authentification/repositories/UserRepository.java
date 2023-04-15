package com.example.pfa_authentification.repositories;

import java.util.List;
import java.util.Optional;

import com.example.pfa_authentification.models.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);

    Boolean existsByEmail(String email);

    User findByPasswordToken(String verificationToken);

    List<User> findByFirstNameIgnoreCase(String firstName);

    List<User> findByLastNameIgnoreCase( String lastName);

}