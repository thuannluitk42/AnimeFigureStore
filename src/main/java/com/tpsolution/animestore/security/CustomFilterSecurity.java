package com.tpsolution.animestore.security;

import com.tpsolution.animestore.repository.UsersRepository;
import com.tpsolution.animestore.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.tpsolution.animestore.service.imp.ClientServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@PropertySource("classpath:application.yml")
public class CustomFilterSecurity {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    CustomJwtFilter customJwtFilter;

    @Autowired
    private JwtUtilsHelper jwtTokenProvider;

//    @Autowired
//    CustomOAuth2UserService oauthUserService;
    @Autowired
    OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    ClientServiceImp userService;

    @Autowired
    UsersRepository usersRepository;

    @Value("${frontend.url}")
    private String frontendUrl;

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of(frontendUrl));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    // Configuring HttpSecurity
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
/*        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> { auth.anyRequest().authenticated();})
                .oauth2Login(oath2 -> {
                    oath2.loginPage("http://localhost:3000/login").permitAll();
                    oath2.successHandler(oAuth2AuthenticationSuccessHandler);
                });*/
        http
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .httpBasic(withDefaults())
                .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    // Password Encoding
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
//    }
//
//    private ClientRegistration googleClientRegistration() {
//        return ClientRegistration.withRegistrationId("google")
//                .clientId("18086882798-q7p0434tqev7pnq32o0lpmkeu9koc468.apps.googleusercontent.com")
//                .clientSecret("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")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .redirectUri("http://localhost:8080/login/oauth2/code/google")
//                .scope("openid", "profile", "email", "address", "phone")
//                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
//                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
//                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
//                .userNameAttributeName(IdTokenClaimNames.SUB)
//                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
//                .clientName("Google")
//                .build();
//    }
}