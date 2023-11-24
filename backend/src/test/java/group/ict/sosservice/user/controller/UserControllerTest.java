package group.ict.sosservice.user.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import group.ict.sosservice.common.annotations.AcceptanceTest;
import group.ict.sosservice.common.annotations.WithMockTestUser;
import group.ict.sosservice.common.exception.ErrorType;
import group.ict.sosservice.user.controller.dto.ChildRegisterRequest;

@AutoConfigureMockMvc
@AcceptanceTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockTestUser
    @DisplayName("보호 대상자를 등록할 수 있다.")
    void givenChildRegisterRequest_thenResponseOK() throws Exception {
        final ChildRegisterRequest registerRequest = ChildRegisterRequest.builder()
            .email("test-user@gmail.com")
            .build();

        final ResultActions result = mockMvc.perform(
            post("/api/v1/child")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        );

        result.andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithMockTestUser
    @DisplayName("보호 대상자를 등록시 email은 필수이다.")
    void givenNonExistEmailRequest_thenErrorResponse() throws Exception {
        final ChildRegisterRequest registerRequest = ChildRegisterRequest.builder()
            .email(null)
            .build();

        final ResultActions result = mockMvc.perform(
            post("/api/v1/child")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        );

        result.andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.error").exists())
            .andExpect(jsonPath("$.error.details").exists());
    }

    @Test
    @WithMockTestUser
    @DisplayName("보호 대상자 등록시 email은 올바른 형식이어야 한다.")
    void givenInvalidEmailRequest_thenErrorResponse() throws Exception {
        final ChildRegisterRequest registerRequest = ChildRegisterRequest.builder()
            .email("Invalid-email.com")
            .build();

        final ResultActions result = mockMvc.perform(
            post("/api/v1/child")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        );

        result.andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.error").exists())
            .andExpect(jsonPath("$.error.details").exists());
    }

    @Test
    @WithMockTestUser
    @DisplayName("보호 대상자 등록시 존재하지 않는 회원의 email인 경우 오류를 발생시킨다.")
    void givenNonExistEmailUser_thenErrorResponse() throws Exception {
        final ChildRegisterRequest registerRequest = ChildRegisterRequest.builder()
            .email("not-exists@gmail.com")
            .build();

        final ResultActions result = mockMvc.perform(
            post("/api/v1/child")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest))
        );

        result.andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success", is(false)))
            .andExpect(jsonPath("$.error").exists())
            .andExpect(jsonPath("$.error.message", is(ErrorType.NOT_FOUND_MEMBER.getMessage())));
    }

    @Test
    @WithMockTestUser
    @DisplayName("보호 대상자를 조회할 수 있다.")
    void get_child() throws Exception {
        final ResultActions result = mockMvc.perform(
            get("/api/v1/child")
                .accept(APPLICATION_JSON)
        );

        result.andDo(print())
            .andExpect(status().isOk());
    }
}