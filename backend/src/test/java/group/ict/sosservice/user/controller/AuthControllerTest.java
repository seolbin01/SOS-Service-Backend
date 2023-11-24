package group.ict.sosservice.user.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import group.ict.sosservice.common.annotations.AcceptanceTest;
import group.ict.sosservice.common.annotations.WithMockTestUser;
import group.ict.sosservice.user.controller.dto.SignUpRequest;
import group.ict.sosservice.user.controller.dto.UserEditRequest;
import lombok.Builder;
import lombok.Getter;

@AutoConfigureMockMvc
@AcceptanceTest
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 할 수 있다.")
    void signup() throws Exception {
        final SignUpRequest signUpRequest = SignUpRequest.builder()
            .name("홍길동")
            .email("test-user@gmail.com")
            .password("test-password")
            .birth(LocalDate.of(1997, 4, 23))
            .profileImage("www.test.com/profile.jpg")
            .phoneNumber("010-1234-5678")
            .build();

        final ResultActions result = mockMvc.perform(
            post("/api/v1/auth/signup")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        );

        result.andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 요청 시 email 값은 필수이다.")
    void givenEmptyName_thenErrorResponse() throws Exception {
        final SignUpRequest signUpRequest = SignUpRequest.builder()
            .email(null)
            .password("1234")
            .name("이승훈")
            .build();

        final ResultActions result = mockMvc.perform(
            post("/api/v1/auth/signup")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        );

        result.andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.error").exists())
            .andExpect(jsonPath("$.error.details").exists())
            .andExpect(jsonPath("$.error.details[0].message", is("이메일은 공백일 수 없습니다.")));
    }

    @Test
    @DisplayName("회원가입 요청 시 email, password, name 값은 필수이다.")
    void givenEmptyEmailAndPassword_thenErrorResponse() throws Exception {
        final SignUpRequest signUpRequest = SignUpRequest.builder().build();

        final ResultActions result = mockMvc.perform(
            post("/api/v1/auth/signup")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        );

        result.andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.error").exists())
            .andExpect(jsonPath("$.error.details").exists())
            .andExpect(jsonPath("$.error.details.length()", is(3)));
    }

    @Test
    @DisplayName("회원가입 요청 시 email은 올바른 형식이어야 한다.")
    void givenInvalidEmail_thenErrorResponse() throws Exception {
        final SignUpRequest signUpRequest = SignUpRequest.builder()
            .email("invalid-email")
            .password("1234")
            .name(null)
            .build();

        final ResultActions result = mockMvc.perform(
            post("/api/v1/auth/signup")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        );

        result.andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.error").exists())
            .andExpect(jsonPath("$.error.details").exists())
            .andExpect(jsonPath("$.error.details.length()", is(2)));
    }

    @Test
    @DisplayName("회원가입 요청 시 핸드폰 번호의 양식은 올바른 형식이어야 한다.")
    void givenInvalidPhoneNumber_thenErrorResponse() throws Exception {
        final SignUpRequest signUpRequest = SignUpRequest.builder()
            .email("test-user@gmail.com")
            .password("1234")
            .name("name")
            .phoneNumber("010-324-345345")
            .build();

        final ResultActions result = mockMvc.perform(
            post("/api/v1/auth/signup")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest))
        );

        result.andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.error").exists())
            .andExpect(jsonPath("$.error.details").exists())
            .andExpect(jsonPath("$.error.details[0].message", is("핸드폰 번호의 양식과 맞지 않습니다. 01x-xxx(x)-xxxx")));
    }

    @Test
    @WithMockTestUser
    @DisplayName("로그인 할 수 있다.")
    void login() throws Exception {
        final EmailPassword loginRequest = EmailPassword.builder()
            .email("test-user@gmail.com")
            .password("1234")
            .build();

        final ResultActions result = mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        );

        result.andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithMockTestUser
    @DisplayName("자동 로그인을 요청할 수 있다.")
    void remember_me_login() throws Exception {
        final EmailPassword loginRequest = EmailPassword.builder()
            .email("test-user@gmail.com")
            .password("1234")
            .build();

        final ResultActions result = mockMvc.perform(
            post("/api/v1/auth/login")
                .param("remember", "true")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        );

        final MvcResult mvcResult = result.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(header().exists("Set-Cookie"))
            .andReturn();

        final String cookies = mvcResult.getResponse()
            .getHeader("Set-Cookie");

        Assertions.assertNotNull(cookies);
        Assertions.assertTrue(cookies.matches("^remember.*"));
    }

    @Test
    @WithMockTestUser
    @DisplayName("로그인 요청 시 올바른 아이디/비밀번호를 입력해야 한다.")
    void givenInvalidCredential_thenErrorResponse() throws Exception {
        final EmailPassword loginRequest = EmailPassword.builder()
            .email("test-user@gmail.com")
            .password("wrong-password")
            .build();

        final ResultActions result = mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        );

        result.andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.error").exists())
            .andExpect(jsonPath("$.error.message", is("아이디 혹은 비밀번호가 올바르지 않습니다.")));
    }

    @Test
    @WithMockTestUser
    @DisplayName("로그아웃 할 수 있다.")
    void logout() throws Exception {
        mockMvc.perform(
                post("/api/v1/auth/logout")
            ).andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithMockTestUser
    @DisplayName("회원정보를 조회할 수 있다.")
    void me() throws Exception {
        final ResultActions result = mockMvc.perform(
            get("/api/v1/auth/me")
        );

        result.andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success", is(true)))
            .andExpect(jsonPath("$.response").exists())
            .andExpect(jsonPath("$.error").isEmpty());
    }

    @Test
    @WithMockTestUser
    @DisplayName("회원정보를 수정할 수 있다.")
    void edit() throws Exception {
        final UserEditRequest editRequest = UserEditRequest.builder()
            .name("modifiedName")
            .password("password-1234")
            .birth(LocalDate.now())
            .profileImage("modified.jpg")
            .phoneNumber("010-1234-5678")
            .build();

        final ResultActions result = mockMvc.perform(
            put("/api/v1/auth/me")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editRequest))
        );

        result.andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithMockTestUser
    @DisplayName("회원정보 수정 시 이름/비밀번호는 필수이다.")
    void givenInvalidEditRequest_thenErrorResponse() throws Exception {
        final UserEditRequest editRequest = UserEditRequest.builder()
            .name(null)
            .password(null)
            .build();

        final ResultActions result = mockMvc.perform(
            put("/api/v1/auth/me")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editRequest))
        );

        result.andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.error").exists())
            .andExpect(jsonPath("$.error.details").exists())
            .andExpect(jsonPath("$.error.details.length()", is(2)));
    }

    @Getter
    @Builder
    static class EmailPassword {

        private String email;

        private String password;
    }
}