package group.ict.sosservice.user.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import group.ict.sosservice.common.exception.ErrorType;
import group.ict.sosservice.user.exception.InvalidMemberException;
import group.ict.sosservice.user.model.Email;
import group.ict.sosservice.user.model.User;
import group.ict.sosservice.user.model.UserEditor;
import group.ict.sosservice.user.model.UserRepository;
import group.ict.sosservice.user.service.dto.SignUpResponse;
import group.ict.sosservice.user.service.dto.UserEditResponse;
import group.ict.sosservice.user.service.dto.UserResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    @Transactional
    public void signup(final SignUpResponse request) {
        validateCredentials(request);
        userRepository.save(mapToUser(request));
    }

    private void validateCredentials(final SignUpResponse request) {
        final Email email = Email.of(request.getEmail());

        if (userRepository.existsUserByEmail(email)) {
            throw new InvalidMemberException(ErrorType.DULICATED_MEMBER_EMAIL);
        }
    }

    private User mapToUser(final SignUpResponse request) {
        final User user = modelMapper.map(request, User.class);
        user.updatePassword(encodePassword(request.getPassword()));
        return user;
    }

    private String encodePassword(final String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Transactional(readOnly = true)
    public UserResponse findOne(final Long userId) {
        return modelMapper.map(findById(userId), UserResponse.class);
    }

    @Transactional
    public void edit(final Long userId, final UserEditResponse request) {
        final User user = findById(userId);
        user.edit(getUserEditor(user, request));
    }

    private User findById(final Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new InvalidMemberException(ErrorType.NOT_FOUND_MEMBER));
    }

    private UserEditor getUserEditor(final User user, final UserEditResponse request) {
        return user.toEditor()
            .name(request.getName())
            .password(encodePassword(request.getPassword()))
            .birth(request.getBirth())
            .profileImage(request.getProfileImage())
            .phoneNumber(request.getPhoneNumber())
            .build();
    }
}
