package group.ict.sosservice.common.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.time.LocalDate;

import org.springframework.context.annotation.Profile;
import org.springframework.security.test.context.support.WithSecurityContext;

import group.ict.sosservice.common.supports.WithMockTestUserSecurityContextFactory;

@Profile("test")
@Retention(RUNTIME)
@WithSecurityContext(factory = WithMockTestUserSecurityContextFactory.class)
public @interface WithMockTestUser {

    String name() default "홍길동";

    String email() default "test-user@gmail.com";

    String password() default "1234";

    String birth() default "1997-04-23";

    String profileImage() default "www.test.com/profile.jpg";

    String phoneNumber() default "010-1234-5678";

    String role() default "USER";
}
