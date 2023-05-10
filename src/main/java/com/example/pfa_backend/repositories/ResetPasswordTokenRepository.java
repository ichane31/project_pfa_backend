package com.example.pfa_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pfa_backend.models.ResetPasswordToken;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long > {

    ResetPasswordToken findByToken(final String token);
    Long removeByToken(String token);
    @Query("SELECT r FROM ResetPasswordToken r WHERE r.expireAt <= :currentTime")
    List<ResetPasswordToken> findExpiredTokens(@Param("currentTime") LocalDateTime currentTime);
}