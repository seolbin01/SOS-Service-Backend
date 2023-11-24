package group.ict.sosservice.user.service.dto;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserEditResponse {

    private String name;

    private String password;

    private LocalDate birth;

    private String profileImage;

    private String phoneNumber;
}
