package ma.ens.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {

        String authHeader = req.getHeader("Authorization");

        if (isBearerToken(authHeader)) {
            String jwt = extractToken(authHeader);
            processAuthentication(jwt);
        }

        chain.doFilter(req, res);
    }

    private boolean isBearerToken(String header) {
        return header != null && header.startsWith("Bearer ");
    }

    private String extractToken(String header) {
        return header.substring(7);
    }

    private void processAuthentication(String token) {

        String userIdentifier = jwtUtil.extractUsername(token);

        if (userIdentifier == null || !jwtUtil.validateToken(token)) {
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(userIdentifier);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}