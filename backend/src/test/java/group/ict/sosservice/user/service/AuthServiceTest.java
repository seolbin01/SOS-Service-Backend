package group.ict.sosservice.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import group.ict.sosservice.common.annotations.AcceptanceTest;
import group.ict.sosservice.common.exception.ErrorType;
import group.ict.sosservice.user.exception.InvalidMemberException;
import group.ict.sosservice.user.model.Email;
import group.ict.sosservice.user.model.Role;
import group.ict.sosservice.user.model.User;
import group.ict.sosservice.user.model.UserRepository;
import group.ict.sosservice.user.service.dto.SignUpResponse;
import group.ict.sosservice.user.service.dto.UserEditResponse;

@AcceptanceTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder encoder;

    @Test
    @DisplayName("회원가입한다.")
    void signup() {
        final SignUpResponse request = SignUpResponse.builder()
            .name("name-1")
            .password("password-1")
            .email("lsh@gmail.com")
            .birth(LocalDate.now())
            .profileImage("test.com/test.jpg")
            .phoneNumber("010-1234-5678")
            .build();

        authService.signup(request);

        final User user = userRepository.findAll().get(0);
        assertEquals(1L, userRepository.count());
        assertEquals(Email.of(request.getEmail()), user.getEmail());
        assertTrue(encoder.matches(request.getPassword(), user.getPassword()));
        assertEquals(request.getName(), user.getName());
        assertEquals(new Email(request.getEmail()), user.getEmail());
        assertEquals(request.getBirth(), user.getBirth());
        assertEquals(request.getProfileImage(), user.getProfileImage());
        assertEquals(request.getPhoneNumber(), user.getPhoneNumber());
    }

    @Test
    @DisplayName("이메일 형식이 올바르지 않으면 오류가 발생한다.")
    void givenInvalidEmail_thenThrowException() {
        final SignUpResponse request = SignUpResponse.builder()
            .name("name-1")
            .password("password-1")
            .email("@gmail.com")
            .build();

        Assertions.assertThatThrownBy(() -> authService.signup(request))
            .isInstanceOf(InvalidMemberException.class)
            .hasMessageContaining(ErrorType.INVALID_MEMBER_EMAIL.getMessage());
    }

    @Test
    @DisplayName("이메일이 중복된 경우 오류가 발생한다.")
    void givenDuplicatedCredential_thenThrowException() {
        userRepository.save(User.builder()
            .name("name-1")
            .password(encoder.encode("password-1"))
            .email("lsh@gmail.com")
            .role(Role.USER)
            .build());

        final SignUpResponse request = SignUpResponse.builder()
            .name("name-1")
            .password("password-1")
            .email("lsh@gmail.com")
            .build();

        Assertions.assertThatThrownBy(() -> authService.signup(request))
            .isInstanceOf(InvalidMemberException.class)
            .hasMessageContaining(ErrorType.DULICATED_MEMBER_EMAIL.getMessage());
    }

    @Test
    @DisplayName("회원 정보를 수정할 수 있다.")
    void edit() {
        final User user = userRepository.save(User.builder()
            .name("name-1")
            .password(encoder.encode("password-1"))
            .email("lsh@gmail.com")
            .birth(LocalDate.now())
            .profileImage("test.jpg")
            .phoneNumber("010-1234-5678")
            .role(Role.USER)
            .build()
        );

        final UserEditResponse requestDto = UserEditResponse.builder()
            .name("modified-name")
            .password("modified-password")
            .build();

        authService.edit(user.getId(), requestDto);

        final User modifiedUser = userRepository.findAll().get(0);
        assertNotEquals(user.getName(), modifiedUser.getName());
        assertFalse(encoder.matches(user.getPassword(), modifiedUser.getPassword()));
    }
}