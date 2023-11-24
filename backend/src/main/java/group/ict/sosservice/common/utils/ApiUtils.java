package group.ict.sosservice.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.FieldError;

import group.ict.sosservice.common.exception.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ApiUtils {

    public static <T> ApiResult<T> success(final T response) {
        return new ApiResult<>(true, response, null);
    }

    public static ApiResult<?> error(final String message) {
        return new ApiResult<>(false, null, new ApiError(message));
    }

    public static ApiResult<?> error(final ErrorType errorType, final List<FieldError> fieldErrors) {
        return new ApiResult<>(false, null, new ApiError(errorType, fieldErrors));
    }

    @Getter
    public static class ApiError {

        private final String message;

        private final List<ErrorDetail> details = new ArrayList<>();

        ApiError(final String message) {
            this.message = message;
        }

        ApiError(final ErrorType errorType) {
            this(errorType.getMessage());
        }

        ApiError(final ErrorType errorType, final List<FieldError> fieldErrors) {
            this(errorType);
            mapDetails(fieldErrors);
        }

        private void mapDetails(final List<FieldError> fieldErrors) {
            for (FieldError error : fieldErrors) {
                details.add(new ErrorDetail(error.getField(), error.getDefaultMessage()));
            }
        }

        @Getter
        @RequiredArgsConstructor
        private static class ErrorDetail {

            private final String param;

            private final String message;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class ApiResult<T> {

        private final boolean success;

        private final T response;

        private final ApiError error;
    }
}
