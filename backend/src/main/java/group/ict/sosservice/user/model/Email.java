package group.ict.sosservice.user.model;

import java.util.Objects;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import group.ict.sosservice.common.exception.ErrorType;
import group.ict.sosservice.user.exception.InvalidMemberException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Email {

    private static final Pattern EMAIL_REGEX = Pattern.compile(
        "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,6}$");

    @Column(name = "email", nullable = false)
    private String value;

    public Email(final String value) {
        validateEmail(value);
        this.value = value;
    }

    public static Email of(final String email) {
        return new Email(email);
    }

    private void validateEmail(final String email) {
        if (email.isBlank() || !isValidPattern(email)) {
            throw new InvalidMemberException(ErrorType.INVALID_MEMBER_EMAIL);
        }
    }

    private boolean isValidPattern(final String email) {
        return EMAIL_REGEX.matcher(email).matches();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Email)) {
            return false;
        }
        final Email other = (Email) o;
        return Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
