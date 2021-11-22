package com.bcp.wstipocambio.util;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import com.bcp.wstipocambio.exception.ApiException;
import com.bcp.wstipocambio.service.JWTService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
public class VerifyTokenUtil {

    public static final String AUTHORIZATION_DATA_ATTRIBUTE = "Authorization-Data";

    public static Map<String, String> getTokenClaims(HttpServletRequest httpServletRequest) {
        final Object authorizationData = httpServletRequest.getAttribute(AUTHORIZATION_DATA_ATTRIBUTE);
        if (!(authorizationData instanceof Map))
            throw new IllegalStateException("authorizationData doesn't exist, it is extracted from token bearer header by the token interceptor");
        return (Map<String, String>) authorizationData;
    }

    /**
     * returns null or "" if not found
     */
    public static String extractToken(String authorizationHeader) {
        if (authorizationHeader != null) {
            final String[] authorizationHeaderParts = authorizationHeader.split(" ");
            if (authorizationHeaderParts.length == 2 && "Bearer".equalsIgnoreCase(authorizationHeaderParts[0])) {
                return authorizationHeaderParts[1];
            }
        }
        return null;
    }

    public static void verifyToken(HttpServletRequest request, JWTService jwtService, String tokenAudience) {
        // this is an offline verification (test if token is signed and so on)
        final String authorizationHeader = request.getHeader("Authorization");
        final String token = extractToken(authorizationHeader);
        if (token != null && !token.isEmpty()) {
            try {
                final Map<String, String> claims = jwtService.parseToken(token, tokenAudience);
                request.setAttribute(AUTHORIZATION_DATA_ATTRIBUTE, claims);
            } catch (JwtException e) {
                log.error("token is not valid: " + token);
                throw new ApiException("token is not valid: " + token, HttpStatus.BAD_REQUEST);
            }
        } else {
            log.error("invalid token: " + authorizationHeader);
            throw new ApiException("invalid token: " + authorizationHeader, HttpStatus.BAD_REQUEST);
        }
    }




}
