package com.bcp.wstipocambio.controller.interceptor;

import javax.servlet.http.*;

import com.bcp.wstipocambio.util.VerifyTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import com.bcp.wstipocambio.service.JWTService;

import static io.jsonwebtoken.Claims.AUDIENCE;

@Slf4j
@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Autowired
    JWTService jwtService;

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler)
            throws Exception {
        if (httpServletRequest.getMethod().equals("OPTIONS")) {
            return true;
        }

        String mappingPath = (String) httpServletRequest.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        log.info(mappingPath);
        if (mappingPath.contains("jwt") || mappingPath.startsWith("/v2/api-docs")) {
            log.info("La información del Usuario en sesión es válida");
            return true;
        }
        log.info("preHandle Header authorization:'{}'", httpServletRequest.getHeader("authorization"));
        VerifyTokenUtil.verifyToken(httpServletRequest, jwtService, AUDIENCE);
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {

    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}