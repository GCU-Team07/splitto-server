package com.team7.spliito_server.advice;

import com.team7.spliito_server.controller.GroupController;
import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(assignableTypes = {
        GroupController.class
})
public class GlobalControllerAdvice {

    // @PathVariable에서 validation 걸릴 시
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ExceptionResponse> invalidInputFromUserHandler(Exception e) {
        String errorMessage = e.getMessage().split(": ")[1];
        log.info(errorMessage);
        return ResponseEntity.badRequest()
                .body(ExceptionResponse.fail(HttpStatus.BAD_REQUEST, errorMessage));
    }

    @Getter
    @NoArgsConstructor
    static class ExceptionResponse {
        private int code;
        private HttpStatus status;
        private String message;

        public ExceptionResponse(HttpStatus status, String message) {
            this.code = status.value();
            this.status = status;
            this.message = message;
        }

        public static ExceptionResponse fail(HttpStatus status, String message) {
            return new ExceptionResponse(status, message);
        }
    }
}
