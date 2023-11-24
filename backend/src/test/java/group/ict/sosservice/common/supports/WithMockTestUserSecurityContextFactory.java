package group.ict.sosservice.common.supports;

import java.time.LocalDate;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import group.ict.sosservice.authentication.service.dto.UserPrincipal;
import group.ict.sosservice.common.annotations.WithMockTestUser;
import group.ict.sosservice.user.model.Role;
import group.ict.sosservice.user.model.User;
import group.ict.sosservice.user.model.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WithMockTestUserSecurityContextFactory
    implements WithSecurityContextFactory<WithMockTestUser> {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Override
    public SecurityContext createSecurityContext(final WithMockTestUser withUser) {
        final User user = userRepository.save(User.builder()
            .name(withUser.name())
            .email(withUser.email())
            .password(encoder.encode(withUser.password()))
            .birth(LocalDate.now())
            .profileImage(withUser.profileImage())
            .phoneNumber(withUser.phoneNumber())
            .role(Role.valueOf(withUser.role()))
            .build()
        );
        final UserPrincipal principal = new UserPrincipal(user);
        final SecurityContext context = SecurityContextHolder.createEmptyContext();

        context.setAuthentication(new UsernamePasswordAuthenticationToken(
            principal,
            principal.getPassword(),
            principal.getAuthorities()
        ));
        return context;
    }
}
