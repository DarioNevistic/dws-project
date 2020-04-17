package com.dnevi.healthcare.security;

import com.nsoft.chiwava.core.commons.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final HandlerExceptionResolver resolver;

    @Autowired
    public JwtAuthenticationEntryPoint(
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException ex) throws IOException {
        log.info("DARIO req {}", request.getUserPrincipal());

        log.info("DARIO res {}", response.getStatus());
        log.info("DARIO res {}", response.getHeaderNames());
        log.error(ex.getLocalizedMessage());

        ThrowableProblem problem = Problem.builder().withTitle("exception.unauthorized")
                .withStatus(Status.UNAUTHORIZED)
                .withDetail("Unauthorized").build();

        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        JsonMapper jsonMapper = new JsonMapper.Builder().withModule(new ProblemModule()).build();
        response.getWriter().write(jsonMapper.toJson(problem));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
