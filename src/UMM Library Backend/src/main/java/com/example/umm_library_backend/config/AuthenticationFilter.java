package com.example.umm_library_backend.config;

import com.example.umm_library_backend.exception.UnauthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if(path.startsWith("/auth/")){
            filterChain.doFilter(request, response);
            return;
        }

//        String header = request.getHeader("Authorization");
//
//        if(header == null || !header.startsWith("Bearer ")){
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setCharacterEncoding("UTF-8");
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().write(
//                    new ObjectMapper().writeValueAsString(
//                            Map.of("status", 401, "message", "Unauthorized")
//                    )
//            );
//            return;
//        }
//
//        String token = header.substring(7);
        filterChain.doFilter(request, response);
    }
}
