package group.ict.sosservice.common.exception;

import lombok.Getter;

@Getter
public enum ErrorType {

    INVALID_REQUEST("잘못된 요청입니다."),

    NOT_FOUND_MEMBER("존재하지 않는 회원입니다."),
    INVALID_MEMBER_NAME("올바르지 않은 이름입니다."),
    INVALID_MEMBER_EMAIL("올바르지 않은 이메일 형식입니다."),
    DULICATED_MEMBER_EMAIL("이미 존재하는 이메일입니다.");

    private final String message;

    ErrorType(final String message) {
        this.message = message;
    }
}
