package org.example.blogapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.val;
import org.example.blogapi.utils.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtTokenValidator extends OncePerRequestFilter {
    private JwtUtils jwtUtils;

    public JwtTokenValidator(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        var jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(jwtToken != null){
            jwtToken = jwtToken.substring(7);

            val decodedJWT = jwtUtils.validateToken(jwtToken);

            val username = jwtUtils.getUsername(decodedJWT);
            val stringAuthorities = jwtUtils.getClaimByName(decodedJWT, "authorities").asString();
            val authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);

            val context = SecurityContextHolder.getContext();
            val authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }

        filterChain.doFilter(request, response);
    }
}
