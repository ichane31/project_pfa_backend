package com.example.pfa_backend.security;

import com.example.pfa_backend.security.Services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extract the authorization header from the request
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Check if the authorization header is present and starts with "Basic "
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {

            try {
                // Decode the base64-encoded username and password from the authorization header
                String base64Credentials = authorizationHeader.substring("Basic ".length());
                byte[] credentialsBytes = Base64.getDecoder().decode(base64Credentials);
                String credentials = new String(credentialsBytes, StandardCharsets.UTF_8);
                String[] splitCredentials = credentials.split(":", 2);

                UserDetails userDetails = userDetailsService.loadUserByUsername(splitCredentials[0]);
                // Create an authentication token with the username and password
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                // Authenticate the token using the authentication manager
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication on the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                // If an error occurs, log it and return a 401 Unauthorized response
                logger.error("Error authenticating user", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized");
                response.getWriter().flush();
                return;
            }
        }

        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }
}
