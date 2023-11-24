package group.ict.sosservice.user.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import group.ict.sosservice.common.model.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Email email;

    private String password;

    private LocalDate birth;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "profile_image")
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    private User child;

    @Builder
    public User(final String name, final String email, final String password,
        final LocalDate birth, final String phoneNumber,
        final String profileImage, final Role role) {
        this.name = name;
        this.email = new Email(email);
        this.password = password;
        this.birth = birth;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
        this.role = role;
    }

    public void updatePassword(final String password) {
        this.password = password;
    }

    public void updateEmail(final String email) {
        this.email = new Email(email);
    }

    public void updateRole(final Role role) {
        this.role = role;
    }

    public void updateChild(final User child) {
        this.child = child;
    }

    public UserEditor.UserEditorBuilder toEditor() {
        return UserEditor.builder()
            .name(name)
            .password(password)
            .birth(birth)
            .profileImage(profileImage)
            .phoneNumber(phoneNumber);
    }

    public void edit(final UserEditor editor) {
        this.name = editor.getName();
        this.password = editor.getPassword();
        this.birth = editor.getBirth();
        this.profileImage = editor.getProfileImage();
        this.phoneNumber = editor.getPhoneNumber();
    }
}
