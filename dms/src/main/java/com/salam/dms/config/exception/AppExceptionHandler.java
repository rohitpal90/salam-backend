package com.salam.dms.config.exception;

import com.salam.dms.model.mappers.RequestHeaderConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.salam.dms.config.exception.AppErrors.*;

@RestControllerAdvice
public class AppExceptionHandler {

    @Autowired
    MessageSource messageSource;

    @ExceptionHandler(AppError.class)
    protected ResponseEntity<?> handleAppError(AppError ex) {
        return appErrorToResponseEntity(ex);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNotFoundError(NoHandlerFoundException ex) {
        AppError error = AppError.create(ex.getBody().getDetail(), URL_NOT_FOUND);
        return appErrorToResponseEntity(error);
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<?> handleForbidden() {
        AppError error = AppError.create("", DMS_APP_ERROR);
        return appErrorToResponseEntity(error);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<?> handlerUserNotFound() {
        return appErrorToResponseEntity(AppError.create(USER_NOT_FOUND));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        var data = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            data.put(fieldName, errorMessage);
        });

        var error = AppError.create(BAD_REQUEST, data, null);
        error.getType().setMessageKey("com.constraint.FormValidation.message");
        return appErrorToResponseEntity(error);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleGlobalException(Exception ex) {
        AppError error;

        if (ex instanceof ErrorResponse errorResponse) {
            return baseErrorToResponseEntity(errorResponse);
        } else if (ex instanceof MethodArgumentTypeMismatchException misEx) {
            if (RequestHeaderConverter.matchMismatchError(misEx)) {
                return appErrorToResponseEntity(AppError.create(REQUEST_NOT_FOUND));
            }
        }

        error = AppError.create("", DMS_APP_ERROR);
        return appErrorToResponseEntity(error);
    }

    private ResponseEntity<?> baseErrorToResponseEntity(ErrorResponse errorResponse) {
        ProblemDetail problemDetail = errorResponse.getBody();
        AppError error = AppError.create(problemDetail.getDetail(), DMS_APP_ERROR);
        return appErrorToResponseEntity(error, errorResponse.getStatusCode());
    }

    private ResponseEntity<?> appErrorToResponseEntity(AppError ex) {
        final AppErrors type = ex.getType();
        return appErrorToResponseEntity(ex, HttpStatusCode.valueOf(type.httpStatus().value()));
    }

    private ResponseEntity<?> appErrorToResponseEntity(AppError ex, HttpStatusCode code) {
        Map<Object, Object> map = new HashMap<>();
        map.put("message", ex.getLocalizedMessage(messageSource));
        if (Objects.nonNull(ex.getData())) {
            map.put("error", Map.of("data", ex.getData()));
        }

        return ResponseEntity.status(code).body(map);
    }

}
