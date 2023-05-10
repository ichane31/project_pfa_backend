package com.example.pfa_backend.services;


import com.example.pfa_backend.exception.NotFoundException;
import com.example.pfa_backend.models.*;
import com.example.pfa_backend.repositories.ResetPasswordTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ResetPasswordTokenService {
    
    @Autowired
    private ResetPasswordTokenRepository resetPasswordTokenRepository;

    public ResetPasswordToken create (User user) {
    
         String token = this.generateUniqueToken();
        // Définir la durée de validité du token (en minutes)
        int tokenValidityDuration = 60;

        // Ajouter la durée de validité au temps actuel
        LocalDateTime tokenExpiryTime = LocalDateTime.now().plusMinutes(tokenValidityDuration);
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        resetPasswordToken.setToken(token);
        resetPasswordToken.setUser(user);
        resetPasswordToken.setExpireAt(tokenExpiryTime);
        return  this.saveToken(resetPasswordToken);

    }

    public ResetPasswordToken saveToken(ResetPasswordToken resetPasswordToken) {
        return resetPasswordTokenRepository.save(resetPasswordToken);
    }

    public ResetPasswordToken getTokenById(Long id) {
        return resetPasswordTokenRepository.findById(id).orElse(null);
    }

    public ResetPasswordToken getTokenByTokenString(String token) {
        return resetPasswordTokenRepository.findByToken(token);
    }

    public List<ResetPasswordToken> getAllTokens() {
        return resetPasswordTokenRepository.findAll();
    }

    public void deleteTokenById(Long id) {
        resetPasswordTokenRepository.deleteById(id);
    }

    public void deleteTokenByTokenString(String token) {
        resetPasswordTokenRepository.removeByToken(token);
    }

    public boolean isTokenExpired(String token) {
        ResetPasswordToken resetPasswordToken = getTokenByTokenString(token);
        return resetPasswordToken != null && resetPasswordToken.isExpired();
    }

    public void deleteExpiredTokens() {
        LocalDateTime now  = LocalDateTime.now();
        List<ResetPasswordToken> expiredTokens = resetPasswordTokenRepository.findExpiredTokens(now);
        resetPasswordTokenRepository.deleteAll(expiredTokens);
    }

    public String generateUniqueToken() {
        String token;
        do {
            UUID uuid = UUID.randomUUID();
            token = uuid.toString();
        } while (this.getTokenByTokenString(token) != null);
        return token;
    }
}
