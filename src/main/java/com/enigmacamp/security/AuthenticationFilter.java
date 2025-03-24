package com.enigmacamp.security;

import com.enigmacamp.model.entity.UserAccount;
import com.enigmacamp.model.dto.response.JwtClaims;
import com.enigmacamp.service.JwtService;
import com.enigmacamp.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final String AUTH_HEADER = "Authorization";
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws IOException {
        try {
            String bearerToken = request.getHeader(AUTH_HEADER);
            System.out.println("Bearer token: " + bearerToken);

            if (bearerToken != null && jwtService.verifyJwtToken(bearerToken)) {
                JwtClaims jwtClaims = jwtService.getClaimsByToken(bearerToken);
                UserAccount userAccount = userService.loadUserById(jwtClaims.getUserAccountId());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userAccount.getUsername(),
                        userAccount.getPassword(),
                        userAccount.getAuthorities()
                );
                authentication.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            String[] errorMessage = e.getMessage().split(" ");
            response.setStatus(Integer.parseInt(errorMessage[0]));
            response.getWriter().println(e.getMessage());
        }
    }
}
