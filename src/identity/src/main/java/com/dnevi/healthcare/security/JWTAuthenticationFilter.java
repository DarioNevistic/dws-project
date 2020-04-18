package com.dnevi.healthcare.security;

import com.dnevi.healthcare.controllers.ProblemHandler;
import com.nsoft.chiwava.core.commons.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zalando.problem.ProblemModule;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private ProblemHandler problemHandler;

    @Value("${app.jwt.header}")
    private String tokenRequestHeader;

    @Value("${app.jwt.header.prefix}")
    private String tokenRequestHeaderPrefix;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Filter the incoming request for a valid token in the request header
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            if (jwt == null && StringUtils.hasText(request.getQueryString())) {
                jwt = org.apache.commons.lang3.StringUtils
                        .substringBefore(request.getQueryString(), "&t=").replace("token=", "");
            }

            if (StringUtils.hasText(jwt) && jwtTokenProvider
                    .validateToken(jwt)) {
                Long userId = jwtTokenProvider.getUserIdFromJWT(jwt);

                UserDetails userDetails = customUserDetailsService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null,
                                userDetails.getAuthorities());
                authentication
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (RuntimeException e) {
            var responseEntity = problemHandler.handleProblem(e);

            response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
            response.setStatus(responseEntity.getStatusCodeValue());

            responseEntity.getHeaders().forEach((k, values) -> {
                values.forEach(v -> {
                    response.setHeader(k, v);
                });
            });

            JsonMapper jsonMapper =
                    new JsonMapper.Builder().withModule(new ProblemModule()).build();
            response.getWriter().write(jsonMapper.toJson(responseEntity.getBody()));
            response.getWriter().close();
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extract the token from the Authorization request header
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(tokenRequestHeader);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenRequestHeaderPrefix)) {
            log.info("Extracted Token: " + bearerToken);
            return bearerToken.replace(tokenRequestHeaderPrefix, "");
        }
        return null;
    }
}