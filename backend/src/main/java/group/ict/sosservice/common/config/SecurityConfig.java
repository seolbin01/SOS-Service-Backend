package group.ict.sosservice.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import group.ict.sosservice.authentication.filter.EmailPasswordAuthFilter;
import group.ict.sosservice.authentication.handler.CustomLogoutHandler;
import group.ict.sosservice.authentication.handler.CustomLogoutSuccessHandler;
import group.ict.sosservice.authentication.handler.LoginFailHandler;
import group.ict.sosservice.authentication.handler.LoginSuccessHandler;
import group.ict.sosservice.authentication.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;

    private final CustomUserDetailsService userDetailsService;

    private final PersistentTokenRepository persistentTokenRepository;

    @Value("${security.authentication.validity-seconds}")
    private long validityInSeconds;

    @Value("${security.authentication.remember-me-key}")
    private String rememberMeKey;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(final HttpSecurity http)
        throws Exception {
        http.cors();
        http.csrf().disable();
        http.headers().frameOptions().sameOrigin();
        http.authorizeHttpRequests((httpRequests) -> httpRequests
            .antMatchers("/docs/**").permitAll()
            .antMatchers("/h2-console/**").permitAll()
            .antMatchers(HttpMethod.POST, "/api/v1/auth/signup").permitAll()
            .antMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
            .anyRequest().authenticated()
        );
        http.addFilterBefore(
            emailPasswordAuthFilter(),
            UsernamePasswordAuthenticationFilter.class
        );
        http.addFilterBefore(
            rememberMeFilter(),
            EmailPasswordAuthFilter.class
        );
        http.logout(logout -> logout
            .logoutUrl("/api/v1/auth/logout")
            .addLogoutHandler(new CustomLogoutHandler(persistentTokenRepository))
            .logoutSuccessHandler(new CustomLogoutSuccessHandler())
        );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowCredentials(true);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public EmailPasswordAuthFilter emailPasswordAuthFilter() {
        EmailPasswordAuthFilter filter = new EmailPasswordAuthFilter(
            "/api/v1/auth/login",
            objectMapper
        );

        filter.setRememberMeServices(rememberMeServices());
        filter.setAuthenticationManager(authenticationManager());

        filter.setAuthenticationFailureHandler(new LoginFailHandler(objectMapper));
        filter.setAuthenticationSuccessHandler(new LoginSuccessHandler(objectMapper));
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());
        return filter;
    }

    @Bean
    RememberMeAuthenticationFilter rememberMeFilter() {
        return new RememberMeAuthenticationFilter(
            authenticationManager(),
            rememberMeServices()
        );
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(
            daoAuthenticationProvider(),
            rememberMeAuthenticationProvider()
        );
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
        return new RememberMeAuthenticationProvider(rememberMeKey);
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        final AbstractRememberMeServices rememberMeServices = getRememberMeServices();
        rememberMeServices.setParameter("remember");
        rememberMeServices.setCookieName("remember");
        rememberMeServices.setAlwaysRemember(false);
        rememberMeServices.setUseSecureCookie(true);
        rememberMeServices.setTokenValiditySeconds(Math.toIntExact(validityInSeconds));
        return rememberMeServices;
    }

    private AbstractRememberMeServices getRememberMeServices() {
        return new PersistentTokenBasedRememberMeServices(
            rememberMeKey,
            userDetailsService,
            persistentTokenRepository
        );
    }
}
