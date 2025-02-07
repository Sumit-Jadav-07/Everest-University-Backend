package com.everestuniversity.filter;

import com.everestuniversity.service.JwtService;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter implements Filter {

    @Autowired
    private JwtService jwtService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String url = req.getRequestURI();
        System.out.println("Url from filter" +  url);
        
        // Skip authentication for public endpoints
        if (isPublicEndpoint(url)) {
            System.out.println("Skipping JWT Authentication for: " + req.getRequestURI());
            chain.doFilter(request, response);
            return;
        }


        String token = req.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove 'Bearer ' prefix
            System.out.println("Token from filter: " + token);

            if (jwtService.validateToken(token)) {
                String email = jwtService.getEmailFromToken(token);
                String role = jwtService.getRoleFromToken(token); // Single role extraction

                // Assign the role dynamically
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, Collections.singletonList(authority));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                System.out.println("Email: " + email);
                System.out.println("Role: " + role);

                chain.doFilter(request, response);
            } else {
                sendErrorResponse(response, "Invalid or expired token");
            }
        } else {
            sendErrorResponse(response, "Missing or invalid Authorization header");
        }
    }

    private boolean isPublicEndpoint(String url) {
        boolean isPublic = url.contains("/public/") || url.contains("/v3/") || url.contains("/swagger-ui/")
                || url.contains("/swagger-resources/") || url.contains("/webjars/") || url.contains("/generate-token");

        System.out.println("Checking public endpoint: " + url + " | isPublic: " + isPublic);
        return isPublic;
    }



    private void sendErrorResponse(ServletResponse response, String message) throws IOException {
        HttpServletResponse res = (HttpServletResponse) response;
        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        res.setContentType("application/json");
        res.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
