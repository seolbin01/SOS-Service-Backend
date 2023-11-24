package group.ict.sosservice.common.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import group.ict.sosservice.user.model.Role;
import group.ict.sosservice.user.model.User;
import group.ict.sosservice.user.service.dto.ChildResponse;
import group.ict.sosservice.user.service.dto.SignUpResponse;
import group.ict.sosservice.user.service.dto.UserResponse;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper
            .getConfiguration()
            .setMatchingStrategy(MatchingStrategies.STRICT)
            .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
            .setFieldMatchingEnabled(true);

        modelMapper.createTypeMap(User.class, UserResponse.class)
            .addMapping(source -> source.getEmail().getValue(), UserResponse::setEmail);

        //TODO: change later
        modelMapper.createTypeMap(SignUpResponse.class, User.class)
            .addMapping(SignUpResponse::getEmail, User::updateEmail)
            .addMapping(source -> Role.USER, User::updateRole);

        modelMapper.createTypeMap(User.class, ChildResponse.class)
            .addMapping(source -> source.getEmail().getValue(), ChildResponse::setEmail);

        return modelMapper;
    }
}
