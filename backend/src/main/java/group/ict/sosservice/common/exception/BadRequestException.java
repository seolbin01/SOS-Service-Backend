package group.ict.sosservice.common.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private final ErrorType errorType;

    public BadRequestException(final ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}
