package group.ict.sosservice.authentication.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;

public class EmailPasswordAuthFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;

    public EmailPasswordAuthFilter(final String loginUrl, final ObjectMapper objectMapper) {
        super(loginUrl);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(
        final HttpServletRequest request,
        final HttpServletResponse response
    ) throws AuthenticationException, IOException {

        final EmailPassword emailPassword = objectMapper.readValue(
            request.getInputStream(),
            EmailPassword.class
        );

        final UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
            emailPassword.getEmail(),
            emailPassword.getPassword()
        );
        token.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(token);
    }

    @Getter
    private static class EmailPassword {

        private String email;

        private String password;
    }
}
