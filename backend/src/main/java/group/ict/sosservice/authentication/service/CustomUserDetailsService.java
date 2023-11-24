package group.ict.sosservice.authentication.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import group.ict.sosservice.authentication.service.dto.UserPrincipal;
import group.ict.sosservice.common.exception.ErrorType;
import group.ict.sosservice.user.model.Email;
import group.ict.sosservice.user.model.User;
import group.ict.sosservice.user.model.UserRepository;
import group.ict.sosservice.user.exception.InvalidMemberException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String username)
        throws UsernameNotFoundException {

        final Email email = Email.of(username);

        final User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new InvalidMemberException(ErrorType.NOT_FOUND_MEMBER));

        return new UserPrincipal(user);
    }
}
