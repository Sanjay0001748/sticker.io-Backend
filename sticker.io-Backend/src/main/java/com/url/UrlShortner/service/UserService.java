package com.url.UrlShortner.service;

import com.url.UrlShortner.Model.User;
import com.url.UrlShortner.Repository.UserRepo;
import com.url.UrlShortner.dtos.LoginRequest;
import com.url.UrlShortner.security.jwt.JwtAuthenticationResponse;
import com.url.UrlShortner.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;
    private UserRepo repo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private JwtUtils jwtUtils;

    @Autowired
    public UserService(AuthenticationManager authenticationManager, UserRepo repo, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }



    public User saveuser(User user)
    {
        String role = user.getRole();
        user.setRole(role);
           user.setPassword(passwordEncoder.encode(user.getPassword()));
           return repo.save(user);
    }

    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);
        return new JwtAuthenticationResponse(jwt);
    }
    public User findByUsername(String name) {
        return repo.findByUsername(name).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + name)
        );
    }
}
