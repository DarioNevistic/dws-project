package com.dnevi.healthcare.controllers;

import com.nsoft.chiwava.core.error.Error;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@ControllerAdvice
@Slf4j(topic = "problem")
public class ProblemHandler implements ProblemHandling {

    private static final Status DEFAULT_STATUS = Status.INTERNAL_SERVER_ERROR;

    @ExceptionHandler
    public ResponseEntity<ThrowableProblem> handleProblem(RuntimeException exception) {
        ThrowableProblem problem;

        log.error("An error occurred", exception);

        if (exception instanceof Error) {
            Error baseException = (Error) exception;

            problem = Problem.builder().withTitle(baseException.getTitle().toString())
                    .withStatus(baseException.getStatus()).withDetail(baseException.getMessage())
                    .build();

            return new ResponseEntity<>(problem,
                    HttpStatus.valueOf(baseException.getStatus().getStatusCode()));
        }

        problem = Problem.builder().withStatus(DEFAULT_STATUS).withDetail(exception.getMessage())
                .build();

        return new ResponseEntity<>(problem, HttpStatus.valueOf(DEFAULT_STATUS.getStatusCode()));
    }
}

