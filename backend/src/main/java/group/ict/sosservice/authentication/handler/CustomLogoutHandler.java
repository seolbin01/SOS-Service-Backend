package group.ict.sosservice.authentication.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {

    private final PersistentTokenRepository tokenRepository;

    @Override
    public void logout(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Authentication authentication
    ) {
        final HttpSession session = request.getSession();

        if (session != null) {
            tokenRepository.removeUserTokens(authentication.getName());
            session.invalidate();
        }
    }
}
