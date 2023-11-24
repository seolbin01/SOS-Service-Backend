package group.ict.sosservice.user.model;

import java.time.LocalDate;

import org.springframework.util.StringUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEditor {

    private String name;

    private String password;

    private LocalDate birth;

    private String profileImage;

    private String phoneNumber;

    public static UserEditorBuilder builder() {
        return new UserEditorBuilder();
    }

    @NoArgsConstructor
    public static class UserEditorBuilder {

        private String name;

        private String password;

        private LocalDate birth;

        private String profileImage;

        private String phoneNumber;

        public UserEditorBuilder name(final String name) {
            if (StringUtils.hasText(name)) {
                this.name = name;
            }
            return this;
        }

        public UserEditorBuilder password(final String password) {
            if (StringUtils.hasText(password)) {
                this.password = password;
            }
            return this;
        }

        public UserEditorBuilder birth(final LocalDate birth) {
            if (birth != null) {
                this.birth = birth;
            }
            return this;
        }

        public UserEditorBuilder profileImage(final String profileImage) {
            if (StringUtils.hasText(profileImage)) {
                this.profileImage = profileImage;
            }
            return this;
        }

        public UserEditorBuilder phoneNumber(final String phoneNumber) {
            if (StringUtils.hasText(phoneNumber)) {
                this.phoneNumber = phoneNumber;
            }
            return this;
        }

        public UserEditor build() {
            return new UserEditor(name, password, birth, profileImage, phoneNumber);
        }
    }
}
