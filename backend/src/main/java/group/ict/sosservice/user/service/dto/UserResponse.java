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
public class UserResponse {

    private String email;

    private String name;

    private LocalDate birth;

    private String phoneNumber;

    private String profileImage;

    public void setEmail(final String email) {
        this.email = email;
    }
}
