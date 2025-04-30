package com.url.UrlShortner.service;


import com.url.UrlShortner.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
@Data
@NoArgsConstructor

public class UserDetailsImp implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private User user;


    public UserDetailsImp(Long id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImp build(User user)
    {

                GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole());

        return new UserDetailsImp(user.getId(),user.getUsername(),user.getEmail(),user.getPassword(),Collections.singleton(authority));
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    //add jwt dependencies
    //generate jwt token
    //validate jwt token
    //extract jwt token from header of http request
    //load username from jwt token
}
