package group.ict.sosservice.user.service.dto;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ChildResponse {

    private Long id;

    private String name;

    private String email;

    private LocalDate birth;

    private String phoneNumber;

    private String profileImage;

    public void setEmail(final String email) {
        this.email = email;
    }
}
