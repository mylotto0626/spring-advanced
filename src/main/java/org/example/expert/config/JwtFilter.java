package org.example.expert.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.config.JwtUtil;
import org.example.expert.domain.user.enums.UserRole;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String url = httpRequest.getRequestURI();

        if (url.startsWith("/auth")) {
            chain.doFilter(request, response);
            return;
        }

        String bearerJwt = httpRequest.getHeader("Authorization");

        if (bearerJwt == null) {
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT 토큰이 필요합니다.");
            return;
        }

        String jwt = jwtUtil.substringToken(bearerJwt);

        try {
            Claims claims = jwtUtil.extractClaims(jwt);
            if (claims == null) {
                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다.");
                return;
            }

            UserRole userRole = UserRole.valueOf(claims.get("userRole", String.class));

            // 레벨 1-4 - JWT 유효성 검사 로직 수정
            // 사용자 정보 userId 속성 저장
            httpRequest.setAttribute("userId", Long.parseLong(claims.getSubject()));
            httpRequest.setAttribute("email", claims.get("email"));
            httpRequest.setAttribute("userRole", userRole);

            if (url.startsWith("/admin") && !UserRole.ADMIN.equals(userRole)) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자 권한이 없습니다.");
                return;
            }

            chain.doFilter(request, response);
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature", e);
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않는 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token", e);
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token", e);
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
        } catch (Exception e) {
            log.error("Invalid JWT token", e);
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않는 JWT 토큰입니다.");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
