package org.example.blogapi.service;

import lombok.val;
import org.example.blogapi.dto.AuthCreateUserRequest;
import org.example.blogapi.dto.AuthLoginRequest;
import org.example.blogapi.dto.AuthResponse;
import org.example.blogapi.entity.RoleEntity;
import org.example.blogapi.entity.UserEntity;
import org.example.blogapi.repository.IRoleRepository;
import org.example.blogapi.repository.IUserRepository;
import org.example.blogapi.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Retrieves user details from the database based on the provided username.
     *
     * @param username the username of the user whose details are to be retrieved
     * @return a UserDetails object representing the user's details fetched from the database
     * @throws UsernameNotFoundException if the user with the given username does not exist in the database
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() ->
            new UsernameNotFoundException("User "+username+" does not exists")
        );

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleEnum().name())));
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

    /**
     * Authenticates a user based on the provided credentials and generates an authentication token.
     *
     * @param authLoginRequest the request object containing username and password for authentication
     * @return an AuthResponse object indicating the outcome of the login attempt, including an access token
     */
    public AuthResponse loginUser(AuthLoginRequest authLoginRequest){
        val username = authLoginRequest.username();
        val password = authLoginRequest.password();

        val authentication = authenticate(username,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        val accessToken = jwtUtils.createToken(authentication);

        return new AuthResponse(
                username,
                "User logged successfully",
                accessToken,
                true
        );
    }

    /**
     * Authenticates a user based on the provided username and password.
     *
     * @param username the username of the user to authenticate
     * @param password the password associated with the username
     * @return an Authentication object representing the authenticated user
     * @throws BadCredentialsException if the provided username or password is invalid
     */
    public Authentication authenticate(String username, String password){
        val userDetails = loadUserByUsername(username);

        if(userDetails == null){
            throw new BadCredentialsException("Invalid username or password");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password.");
        }

        return new UsernamePasswordAuthenticationToken(userDetails.getPassword(), userDetails.getPassword(), userDetails.getAuthorities());
    }

    public AuthResponse createUser(AuthCreateUserRequest authCreateUserRequest) {
        val username = authCreateUserRequest.username();
        val password = authCreateUserRequest.password();
        val roleRequest = authCreateUserRequest.roleRequest().roleListName();

        Set<RoleEntity> roleEntitySet = new HashSet<>(roleRepository.findRoleEntitiesByRoleEnumIn(roleRequest));
        if(roleEntitySet.isEmpty()){
            throw new IllegalArgumentException("The specified roles do not exists");
        }

        val userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(roleEntitySet)
                .isEnabled(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .build();
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("The specified username already exists");
        }
        val savedUserEntity = userRepository.save(userEntity);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        savedUserEntity.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleEnum().name())));
        savedUserEntity.getRoles().stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        val accessToken = jwtUtils.createToken(
            new UsernamePasswordAuthenticationToken(savedUserEntity.getUsername(), savedUserEntity.getPassword(), authorities)
        );

        return new AuthResponse(
            savedUserEntity.getUsername(),
            "User created successfully",
            accessToken,
            true
        );
    }
}
