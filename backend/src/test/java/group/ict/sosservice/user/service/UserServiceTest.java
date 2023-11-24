package group.ict.sosservice.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import group.ict.sosservice.common.annotations.AcceptanceTest;
import group.ict.sosservice.common.annotations.WithMockTestUser;
import group.ict.sosservice.user.model.Role;
import group.ict.sosservice.user.model.User;
import group.ict.sosservice.user.model.UserRepository;
import group.ict.sosservice.user.service.dto.ChildResponse;

@AcceptanceTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    private User parent;

    private User child;

    @BeforeEach
    void setUp() {
        parent = userRepository.save(User.builder()
            .name("parent")
            .password(encoder.encode("password-1"))
            .email("parent@gmail.com")
            .role(Role.USER)
            .build()
        );

        child = userRepository.save(User.builder()
            .name("child")
            .password(encoder.encode("password-1"))
            .email("child@gmail.com")
            .role(Role.USER)
            .build()
        );
    }

    @Test
    @DisplayName("보호 대상자를 등록할 수 있다.")
    void register_child() {
        userService.registerChild(parent.getId(), child.getEmail().getValue());

        final User actual = parent.getChild();
        assertEquals(child.getName(), actual.getName());
        assertEquals(child.getBirth(), actual.getBirth());
        assertEquals(child.getPassword(), actual.getPassword());
        assertEquals(child.getEmail(), actual.getEmail());
    }

    @Test
    @WithMockTestUser
    @DisplayName("보호 대상자를 조회할 수 있다.")
    void get_child() {
        userService.registerChild(parent.getId(), child.getEmail().getValue());

        final ChildResponse actual = userService.findChild(parent.getId());
        assertEquals(child.getId(), actual.getId());
        assertEquals(child.getName(), actual.getName());
        assertEquals(child.getEmail().getValue(), actual.getEmail());
        assertEquals(child.getBirth(), actual.getBirth());
        assertEquals(child.getPhoneNumber(), actual.getPhoneNumber());
        assertEquals(child.getProfileImage(), actual.getProfileImage());
    }

    @Test
    @WithMockTestUser
    @DisplayName("보호 대상자가 존재하지 않으면 빈값을 반환한다.")
    void givenNoChildUser_thenReturnEmpty() {
        final ChildResponse actual = userService.findChild(parent.getId());
        final ChildResponse empty = ChildResponse.builder().build();

        assertEquals(empty.getId(), actual.getId());
        assertEquals(empty.getName(), actual.getName());
        assertEquals(empty.getEmail(), actual.getEmail());
        assertEquals(empty.getBirth(), actual.getBirth());
        assertEquals(empty.getPhoneNumber(), actual.getPhoneNumber());
        assertEquals(empty.getProfileImage(), actual.getProfileImage());
    }
}