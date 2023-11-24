package group.ict.sosservice.authentication.handler;

import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final Authentication authentication
    ) throws IOException, ServletException {
        response.setCharacterEncoding(UTF_8.displayName());
        response.setStatus(SC_OK);
        response.setContentType(APPLICATION_JSON_VALUE);
    }
}
