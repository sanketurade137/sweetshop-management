package com.example.SweetshopManagementSystem.Config;

import com.example.SweetshopManagementSystem.Service.JwtUtilService;
import com.example.SweetshopManagementSystem.Service.MyuserServiceimpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtilService jwtUtilService;
    @Autowired
    private MyuserServiceimpl myuserServiceimpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip JWT validation for public paths
        if (path.startsWith("/api/auth/login") || path.startsWith("/api/auth/register") ) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractToken(request);
        if (token != null) {
            final String userName = jwtUtilService.extractUserName(token);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (userName != null && authentication == null) {
                UserDetails userDetails = myuserServiceimpl.loadUserByUsername(userName);
                if (jwtUtilService.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }


    public String extractToken(HttpServletRequest request){
        String header=request.getHeader("Authorization");
        if(header!=null && header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null;
    }

}
