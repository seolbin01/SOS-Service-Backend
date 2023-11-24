package group.ict.sosservice.user.exception;

import group.ict.sosservice.common.exception.BadRequestException;
import group.ict.sosservice.common.exception.ErrorType;

public class InvalidMemberException extends BadRequestException {

    public InvalidMemberException(final ErrorType errorType) {
        super(errorType);
    }
}
