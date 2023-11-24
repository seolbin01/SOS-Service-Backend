package group.ict.sosservice.user.controller;

import static group.ict.sosservice.common.utils.ApiUtils.success;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import group.ict.sosservice.authentication.service.dto.UserPrincipal;
import group.ict.sosservice.common.utils.ApiUtils;
import group.ict.sosservice.user.controller.dto.SignUpRequest;
import group.ict.sosservice.user.controller.dto.UserEditRequest;
import group.ict.sosservice.user.service.AuthService;
import group.ict.sosservice.user.service.dto.SignUpResponse;
import group.ict.sosservice.user.service.dto.UserEditResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final ModelMapper modelMapper;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid final SignUpRequest request) {
        authService.signup(modelMapper.map(request, SignUpResponse.class));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ApiUtils.ApiResult<?> me(@AuthenticationPrincipal final UserPrincipal userPrincipal) {
        return success(authService.findOne(userPrincipal.getUserId()));
    }

    @PutMapping("/me")
    public ResponseEntity<?> edit(
        @AuthenticationPrincipal final UserPrincipal userPrincipal,
        @RequestBody @Valid final UserEditRequest request
    ) {
        authService.edit(
            userPrincipal.getUserId(),
            modelMapper.map(request, UserEditResponse.class)
        );
        return ResponseEntity.ok().build();
    }
}
