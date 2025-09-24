package com.example.housingmanagementsystem.Configs;

import com.example.housingmanagementsystem.UtilityClasses.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService; //used to load user details from db given a username

    public JWTAuthenticationFilter(JWTUtil jwtUtil,UserDetailsService userDetailsService){
        this.jwtUtil=jwtUtil;
        this.userDetailsService=userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest,HttpServletResponse servletResponse,FilterChain filterChain) throws ServletException, IOException{
        String authHeader=servletRequest.getHeader("Authorization"); //gets the Authorization header from the request

        if(authHeader!=null && authHeader.startsWith("Bearer")){
            String jwt=authHeader.substring(7);
            String username=jwtUtil.extractUsername(jwt);

            //If username exists and there's no security context,meaning the user hasn't logged in,proceed to the next step
            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                UserDetails userDetails= userDetailsService.loadUserByUsername(username);

                //Validates the token and if valid,set's up a Spring security authentication object containing the user details and roles.Password is set to null since it's already authenticated
                if (jwtUtil.validateToken(jwt,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                    //Adds extra request info(IP,session) to the authentication object
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(servletRequest));

                    //Marks the user as authenticated in Spring's security context
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }

        //Enables the request to continue to the other controllers
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
