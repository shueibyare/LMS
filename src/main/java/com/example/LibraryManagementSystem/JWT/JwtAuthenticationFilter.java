package com.example.LibraryManagementSystem.JWT;

import com.example.LibraryManagementSystem.Repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String username;

        //Check if Authorization header is present and starts with "Bearer"
        if(authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        //Extract JWT token from header
        jwtToken = authHeader.substring(7);
        username= JwtService.extractUsername(jwtToken);

        //Check if we have username and no authentication exits
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

            //Get the user details from DB
            var userDetails = userRepository.findByUsername(username)
                    .orElseThrow(()-> new RuntimeException("user not found"));

            //validate the token
            if(JwtService.isTokenValid(jwtToken, userDetails)) {

                //create the authentication with user roles
                List<SimpleGrantedAuthority> authorities = userDetails.getRoles()
                                                                       .stream()
                                                                        .map(SimpleGrantedAuthority::new)
                                                                        .collect(Collectors.toList());
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        authorities
                );

                //set the authentication details
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //update the security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        }
        filterChain.doFilter(request, response);

    }

}
