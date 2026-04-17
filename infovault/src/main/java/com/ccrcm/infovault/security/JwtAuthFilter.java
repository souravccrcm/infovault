package com.ccrcm.infovault.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AppUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String header = request.getHeader("Authorization");
            String token = null;

            if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
                token = header.substring(7);
            }

            if (token != null) {

                // 🔥 VALIDATION (throws exception if invalid/expired)
                Claims claims = jwtUtil.getClaims(token);

                if (jwtUtil.isRefreshToken(token)) {
                    writeErrorResponse(response, "INVALID_TOKEN", "Refresh token not allowed here", 401);
                    return;
                }

                if (SecurityContextHolder.getContext().getAuthentication() == null) {

                    Long userId = jwtUtil.getUserId(token);

                    AppUserDetails userDetails =
                            (AppUserDetails) userDetailsService.loadByUserId(userId);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        } catch (ExpiredJwtException ex) {
            writeErrorResponse(response, "TOKEN_EXPIRED", "Access token expired", 401);
            return;
        } catch (MalformedJwtException | SignatureException ex) {
            writeErrorResponse(response, "INVALID_TOKEN", "Invalid token", 401);
            return;
        } catch (Exception ex) {
            writeErrorResponse(response, "AUTH_ERROR", "Authentication failed", 401);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void writeErrorResponse(HttpServletResponse response,
                                    String error,
                                    String message,
                                    int status) throws IOException {

        response.setStatus(status);
        response.setContentType("application/json");

        String body = """
            {
                "success": false,
                "message": "%s",
                "error": "%s"
            }
            """.formatted(message, error);

        response.getWriter().write(body);
    }
}