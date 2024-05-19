package com.tpsolution.animestore.security;

import com.tpsolution.animestore.entity.Users;
import com.tpsolution.animestore.repository.UsersRepository;
import com.tpsolution.animestore.security.oauth2.CustomOAuth2UserService;
import com.tpsolution.animestore.service.imp.ClientServiceImp;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.List;

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

    @Autowired
    CustomOAuth2UserService oauthUserService;

    @Autowired
    ClientServiceImp userService;

    @Autowired
    UsersRepository usersRepository;

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("/**"));
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
        //httpBasic, csrf, formLogin, rememberMe, logout, session disable
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .formLogin(frmLogin -> frmLogin.disable())
            .rememberMe(rmMe -> rmMe.disable())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //Set permissions for requests
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/welcome", "/auth/generateToken","/auth/test-request","/auth/logout", "/oauth2/authorization/**","/auth/logout-success","/auth/login-success").permitAll())
            .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/user/**").authenticated())
            .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/product/**").authenticated())
            .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/category/**").authenticated())
            .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/admin/**").authenticated())
        //oauth2Login
            .oauth2Login(oauth2LoginCustomizer -> oauth2LoginCustomizer
                .userInfoEndpoint(userInfoEndpointCustomizer -> userInfoEndpointCustomizer.userService(oauthUserService))
                    .successHandler(new AuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

                            UserInfoDetails userInfoDetails = (UserInfoDetails) authentication.getPrincipal();

                            //Generate JWT
                            String tokenInfo = jwtTokenProvider.generateToken(userInfoDetails.getUsername());
                            Users u = usersRepository.findUsersByEmailAndIsDelete(userInfoDetails.getUsername(),Boolean.FALSE);
                            u.setToken(tokenInfo);
                            usersRepository.save(u);
                            
                            //Redirect to a success page after user creation
                            response.sendRedirect("/auth/login-success");

                        }

                    })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        response.sendRedirect("/auth/login-failed");
                    }
                })
            );
        http.logout(logoutConfig -> logoutConfig.clearAuthentication(true).deleteCookies("JSESSIONID"));
        //jwt filter settings
        http.authenticationProvider(authenticationProvider()).addFilterBefore(customJwtFilter, UsernamePasswordAuthenticationFilter.class);
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