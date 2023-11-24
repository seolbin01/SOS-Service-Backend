package group.ict.sosservice.common.exception;

import static group.ict.sosservice.common.utils.ApiUtils.error;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import group.ict.sosservice.common.utils.ApiUtils.ApiResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResult<?>> handleBadRequestException(
        final MethodArgumentNotValidException e
    ) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error(ErrorType.INVALID_REQUEST, new ArrayList<>(e.getFieldErrors())));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResult<?>> handleBadRequestException(final BadRequestException e) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error(e.getMessage()));
    }
}
