package com.example.pfa_backend.controllers;

import com.example.pfa_backend.exception.NotFoundException;
import com.example.pfa_backend.models.ResetPasswordToken;
import com.example.pfa_backend.services.ResetPasswordTokenService;
import com.example.pfa_backend.services.UserService;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resetPasswordTokens")
public class ResetPasswordTokenController {

    @Autowired
    private ResetPasswordTokenService resetPasswordTokenService;

    @GetMapping("/{id}")
    public ResponseEntity<ResetPasswordToken> getTokenById(@PathVariable Long id) {
        ResetPasswordToken token = resetPasswordTokenService.getTokenById(id);
        if (token == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<ResetPasswordToken> getTokenByTokenString(@PathVariable String token) {
        ResetPasswordToken resetPasswordToken = resetPasswordTokenService.getTokenByTokenString(token);
        if (resetPasswordToken == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(resetPasswordToken, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<ResetPasswordToken>> getAllTokens() {
        List<ResetPasswordToken> tokens = resetPasswordTokenService.getAllTokens();
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTokenById(@PathVariable Long id) {
        resetPasswordTokenService.deleteTokenById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/token/{token}")
    public ResponseEntity<Void> deleteTokenByTokenString(@PathVariable String token) {
        resetPasswordTokenService.deleteTokenByTokenString(token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/token/{token}/expired")
    public ResponseEntity<Boolean> isTokenExpired(@PathVariable String token) {
        boolean isExpired = resetPasswordTokenService.isTokenExpired(token);
        return new ResponseEntity<>(isExpired, HttpStatus.OK);
    }

    @DeleteMapping("/expired")
    public ResponseEntity<Void> deleteExpiredTokens() {
        resetPasswordTokenService.deleteExpiredTokens();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
