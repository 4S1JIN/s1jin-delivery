package com.fourseason.delivery.global.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fourseason.delivery.global.auth.CustomPrincipal;
import com.fourseason.delivery.global.auth.JwtUtil;
import com.fourseason.delivery.global.exception.CustomException;
import com.fourseason.delivery.global.exception.ErrorCode;
import com.fourseason.delivery.global.exception.ErrorResponseEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.fourseason.delivery.global.auth.exception.AuthErrorCode.*;

@Slf4j
@RequiredArgsConstructor
public class JwtCheckFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // 지정한 경로로 시작되는 요청은 모두 제외 됨. 패턴은 적용안 됨.
    private static final List<String> EXCLUDED_PATHS = List.of(
            "/api/sign",
            "/api/api-docs",
            "/api/swagger-ui",
            "/api/v2/api-docs",
            "/api/swagger-resources",
            "/v3/api-docs",
            "/api/webjars",
            "/page",
            "/css"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        System.out.println(path);
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            //  Bearer 를 제외한 순수 토큰 값
            String accessToken = getTokenFromRequest(request);

            Claims claims = jwtUtil.validateToken(accessToken);
//            log.info(claims.toString());

            CustomPrincipal principal = new CustomPrincipal(claims.getSubject(), claims.get("id", Long.class),
                    claims.get("role", String.class));

            // 토큰 검증을 통해 가져온 claims 으로 Authentication 객체 생성
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            Collections.singletonList(
                                    new SimpleGrantedAuthority("ROLE_" + claims.get("role").toString())
                            )
                    );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
            setErrorResponse(response, ACCESS_TOKEN_EXPIRED);
        } catch (JwtException e) {
            log.error(e.getMessage());
            setErrorResponse(response, ACCESS_TOKEN_NOT_AVAILABLE);
        } catch (CustomException e) {
            setErrorResponse(response, e.getErrorCode());
        }
    }

    // header 에서 JWT 가져오기
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        } else {
            // Access Token 이 없거나 prefix 가 Bearer 가 아닌 경우
            throw new CustomException(ACCESS_TOKEN_NOT_FOUND);
        }
    }

    // error handler
    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.httpStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponseEntity errorResponse = ErrorResponseEntity.builder()
                .status(errorCode.httpStatus().value())
                .message(errorCode.message())
                .build();
        String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);

        response.getWriter().write(jsonResponse);
    }
}
