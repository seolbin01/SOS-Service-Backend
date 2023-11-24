package group.ict.sosservice.user.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import group.ict.sosservice.common.annotations.AcceptanceTest;
import group.ict.sosservice.common.annotations.WithMockTestUser;
import group.ict.sosservice.user.controller.dto.SignUpRequest;
import group.ict.sosservice.user.controller.dto.UserEditRequest;
import lombok.Builder;
import lombok.Getter;

@AcceptanceTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(
    uriScheme = "https",
    uriHost = "api.ict-sos-service.com",
    uriPort = 443
)
public class AuthControllerDocTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입")
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
            .andExpect(status().isOk())
            .andDo(document("user-signup",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("name").description("회원 이름"),
                    fieldWithPath("email").description("이메일 주소")
                        .attributes(key("constraint").value(
                            "형식에 맞춰 작성 (^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,6}$)")),
                    fieldWithPath("password").description("비밀번호"),
                    fieldWithPath("birth")
                        .description("생년월일").optional()
                        .attributes(key("constraint").value("형식에 맞춰 작성 (yyyy-MM-dd)")),
                    fieldWithPath("profileImage").description("프로필 URL").optional(),
                    fieldWithPath("phoneNumber")
                        .description("전화번호").optional()
                        .attributes(key("constraint").value("형식에 맞춰 작성 (01x-xxx(x)-xxxx) "))
                )
            ));
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
            .andDo(document("user-signup-invalid-email",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("name").description("회원 이름"),
                    fieldWithPath("email").description("이메일 주소")
                        .attributes(key("constraint").value(
                            "형식에 맞춰 작성 (^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,6}$)")),
                    fieldWithPath("password").description("비밀번호"),
                    fieldWithPath("birth")
                        .description("생년월일").optional()
                        .attributes(key("constraint").value("형식에 맞춰 작성 (yyyy-MM-dd)")),
                    fieldWithPath("profileImage").description("프로필 URL").optional(),
                    fieldWithPath("phoneNumber")
                        .description("전화번호").optional()
                        .attributes(key("constraint").value("형식에 맞춰 작성 (01x-xxx(x)-xxxx) "))
                ),
                responseFields(
                    fieldWithPath("success").description("응답 성공 여부"),
                    fieldWithPath("response").type(OBJECT).description("응답 객체").optional(),
                    fieldWithPath("error").type(OBJECT).ignored(),
                    fieldWithPath("error.message").description("오류 메시지"),
                    fieldWithPath("error.details").type(ARRAY).description("오류 필드 목록").optional(),
                    fieldWithPath("error.details[].param").description("오류 필드명").optional(),
                    fieldWithPath("error.details[].message").description("오류 필드 상세 메시지").optional()
                )
            ));
    }

    @Test
    @WithMockTestUser
    @DisplayName("로그인")
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
            .andExpect(status().isOk())
            .andDo(document("user-login",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").description("이메일 주소")
                        .attributes(key("constraint").value(
                            "형식에 맞춰 작성 (^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,6}$)")),
                    fieldWithPath("password").description("비밀번호")
                ),
                responseFields(
                    fieldWithPath("success").description("응답 성공 여부"),
                    fieldWithPath("response").type(NUMBER).description("로그인한 사용자의 ID"),
                    fieldWithPath("error").type(OBJECT).ignored()
                )
            ));
    }

    @Test
    @WithMockTestUser
    @DisplayName("로그인 요청 시 올바른 아이디/비밀번호를 입력해야 한다.")
    void givenInvalidCredential_thenErrorResponse() throws Exception {
        final EmailPassword loginRequest = EmailPassword.builder()
            .email("test-user@gmail.com")
            .password("wrong")
            .build();

        final ResultActions result = mockMvc.perform(
            post("/api/v1/auth/login")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        );

        result.andDo(print())
            .andExpect(status().isBadRequest())
            .andDo(document("user-login-error",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("email").description("이메일 주소")
                        .attributes(key("constraint").value(
                            "형식에 맞춰 작성 (^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,6}$)")),
                    fieldWithPath("password").description("비밀번호")
                ),
                responseFields(
                    fieldWithPath("success").description("응답 성공 여부"),
                    fieldWithPath("response").type(NUMBER).description("로그인한 사용자의 ID").optional(),
                    fieldWithPath("error").type(OBJECT).ignored(),
                    fieldWithPath("error.message").description("오류 메시지"),
                    fieldWithPath("error.details").type(ARRAY).description("오류 필드 목록").optional()
                )
            ));
    }

    @Test
    @WithMockTestUser
    @DisplayName("회원 정보 조회")
    void me() throws Exception {
        final ResultActions result = mockMvc.perform(
            get("/api/v1/auth/me")
                .accept(APPLICATION_JSON)
        );

        result.andDo(print())
            .andExpect(status().isOk())
            .andDo(document("user-me",
                preprocessResponse(prettyPrint()),
                responseFields(
                    fieldWithPath("success").type(BOOLEAN).description("응답 성공 여부"),
                    fieldWithPath("response").type(OBJECT).description("회원 정보"),
                    fieldWithPath("response.name").type(STRING).description("회원 이름"),
                    fieldWithPath("response.email").type(STRING).description("이메일 주소"),
                    fieldWithPath("response.birth").type(STRING).description("생년월일").optional(),
                    fieldWithPath("response.profileImage").type(STRING).description("프로필 URL").optional(),
                    fieldWithPath("response.phoneNumber").type(STRING).description("전화번호").optional(),
                    fieldWithPath("error").type(OBJECT).ignored()
                )
            ));
    }

    @Test
    @WithMockTestUser
    @DisplayName("회원 정보 수정")
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
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editRequest))
        );

        result.andDo(print())
            .andExpect(status().isOk())
            .andDo(document("user-edit",
                preprocessRequest(prettyPrint()),
                requestFields(
                    fieldWithPath("name").description("회원 이름"),
                    fieldWithPath("password").description("비밀번호"),
                    fieldWithPath("birth")
                        .description("생년월일").optional()
                        .attributes(key("constraint").value("형식에 맞춰 작성 (yyyy-MM-dd)")),
                    fieldWithPath("profileImage").description("프로필 URL").optional(),
                    fieldWithPath("phoneNumber")
                        .description("전화번호").optional()
                        .attributes(key("constraint").value("형식에 맞춰 작성 (01x-xxx(x)-xxxx) "))
                )
            ));
    }

    @Test
    @WithMockTestUser
    @DisplayName("회원 정보 수정 시 이름/비밀번호는 필수이다.")
    void givenInvalidEditRequest_thenErrorResponse() throws Exception {
        final UserEditRequest editRequest = UserEditRequest.builder()
            .name(null)
            .password(null)
            .birth(LocalDate.now())
            .profileImage("modified.jpg")
            .phoneNumber("010-1234-5678")
            .build();

        final ResultActions result = mockMvc.perform(
            put("/api/v1/auth/me")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editRequest))
        );

        result.andDo(print())
            .andExpect(status().isBadRequest())
            .andDo(document("user-edit-error",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("name").description("회원 이름"),
                    fieldWithPath("password").description("비밀번호"),
                    fieldWithPath("birth")
                        .description("생년월일").optional()
                        .attributes(key("constraint").value("형식에 맞춰 작성 (yyyy-MM-dd)")),
                    fieldWithPath("profileImage").description("프로필 URL").optional(),
                    fieldWithPath("phoneNumber")
                        .description("전화번호").optional()
                        .attributes(key("constraint").value("형식에 맞춰 작성 (01x-xxx(x)-xxxx) "))
                ),
                responseFields(
                    fieldWithPath("success").description("응답 성공 여부"),
                    fieldWithPath("response").type(OBJECT).ignored(),
                    fieldWithPath("error").type(OBJECT).ignored(),
                    fieldWithPath("error.message").description("오류 메시지"),
                    fieldWithPath("error.details").type(ARRAY).description("오류 필드 목록").optional(),
                    fieldWithPath("error.details[].param").description("오류 필드명").optional(),
                    fieldWithPath("error.details[].message").description("오류 필드 상세 메시지").optional()
                )
            ));
    }

    @Getter
    @Builder
    private static class EmailPassword {

        private String email;

        private String password;
    }
}
