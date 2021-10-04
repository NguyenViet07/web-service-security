package com.music.webservice.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.music.webservice.response.ResponseCode;
import com.music.webservice.response.ResponseResult;
import com.music.webservice.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private TokenService tokenService;

    @Value("${public.api}")
    private String publicApi;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String message = ResponseCode.SuccessCode.ACCESS_DENIED;
        List<String> listPublicApi = Arrays.asList(publicApi.split(","));
        try {
            String token = jwtTokenProvider.getJwtFromRequest(httpServletRequest);
            String uri = httpServletRequest.getRequestURI();
            if (checkPublicApi(uri, listPublicApi)) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
            if (token == null) {
                message = ResponseCode.SuccessCode.UNAUTHORIZED;
            }
            if ((StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) && tokenService.existsToken(token) != null) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
        } catch (Exception e) {
            message = e.getMessage();
            log.error("co loi xay ra: " + e);
        }
        ResponseResult response = new ResponseResult(message, null);
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(response));
    }

    public boolean checkPublicApi(String uri, List<String> urls) {
        for (String url : urls) {
            if (uri.contains(url)) {
                return true;
            }
        }
        return false;
    }
}
