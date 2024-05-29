package com.tpsolution.animestore.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

// This class helps us to validate the generated jwt token
@Component
public class CustomJwtFilter extends OncePerRequestFilter {
    @Autowired
    JwtUtilsHelper jwtUtilsHelper;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    private String getTokenFromHeader(HttpServletRequest request){
        String authHeader  = request.getHeader("Authorization");
        String token = null;
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
        }
        return token;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromHeader(request);
        String username = null;
        if (token != null){
            username = jwtUtilsHelper.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtUtilsHelper.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        filterChain.doFilter(request,response);
    }

}
