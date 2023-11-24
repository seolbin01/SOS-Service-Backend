package group.ict.sosservice.user.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChildRegisterRequest {

    @NotBlank(message = "이메일은 공백일 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,6}$",
        message = "이메일 형식이 올바르지 않습니다.")
    private String email;
}
