package com.team7.spliito_server.advice;

import com.team7.spliito_server.controller.GroupController;
import com.team7.spliito_server.controller.PaymentController;
import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@ControllerAdvice(assignableTypes = {
        GroupController.class,
        PaymentController.class
})
public class GlobalControllerAdvice {

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionResponse> bindExHandler(BindException e, RedirectAttributes redirectAttributes) {
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        String errorMessage = objectError.getDefaultMessage();

        log.info(errorMessage);

        return ResponseEntity.badRequest()
                .body(ExceptionResponse.fail(HttpStatus.BAD_REQUEST, errorMessage));
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ExceptionResponse> illegalArgumentExHandler(Exception e) {
        String errorMessage = e.getMessage();
        log.info(errorMessage);
        return ResponseEntity.badRequest()
                .body(ExceptionResponse.fail(HttpStatus.BAD_REQUEST, errorMessage));
    }

    // @PathVariable에서 validation 걸릴 시 (ex) -1L)
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ExceptionResponse> invalidInputFromUserHandler(Exception e) {
        String errorMessage = e.getMessage().split(": ")[1];
        log.info(errorMessage);
        return ResponseEntity.badRequest()
                .body(ExceptionResponse.fail(HttpStatus.BAD_REQUEST, errorMessage));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> unExpectedExHandler(Exception e) {
        String errorMessage = e.getMessage();

        log.error(errorMessage);
        e.printStackTrace();

        return ResponseEntity.internalServerError()
                .body(ExceptionResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage));
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
