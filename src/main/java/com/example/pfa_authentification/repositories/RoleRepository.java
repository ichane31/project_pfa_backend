package com.example.pfa_authentification.repositories;

import java.util.Optional;

import com.example.pfa_authentification.models.ERole;
import com.example.pfa_authentification.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}