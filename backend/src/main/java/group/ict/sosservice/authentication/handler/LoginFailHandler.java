package group.ict.sosservice.authentication.handler;

import static group.ict.sosservice.common.utils.ApiUtils.error;
import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import group.ict.sosservice.common.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class LoginFailHandler implements AuthenticationFailureHandler {

    private static final String ERR_MESSAGE = "아이디 혹은 비밀번호가 올바르지 않습니다.";

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(
        final HttpServletRequest request,
        final HttpServletResponse response,
        final AuthenticationException exception
    ) throws IOException {
        log.error("[인증오류] {}", ERR_MESSAGE);
        sendResponse(response, error(ERR_MESSAGE));
    }

    private void sendResponse(
        final HttpServletResponse response,
        final ApiUtils.ApiResult<?> error
    ) throws IOException {
        response.setCharacterEncoding(UTF_8.displayName());
        response.setStatus(SC_BAD_REQUEST);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
