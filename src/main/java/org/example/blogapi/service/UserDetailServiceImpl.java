package org.example.blogapi.service;

import org.example.blogapi.entity.UserEntity;
import org.example.blogapi.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private IUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Get username, password, roles, permissions from db
        UserEntity user = repository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User "+username+" does not exists")
        );

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        user.getRoles().forEach(
            role -> authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleEnum().name()))
        );
        user.getRoles().stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(
            user.getUsername(),
            user.getPassword(),
            user.isEnabled(),
            user.isAccountNoExpired(),
            user.isCredentialNoExpired(),
            user.isAccountNoLocked(),
            authorities
        );
    }
}
