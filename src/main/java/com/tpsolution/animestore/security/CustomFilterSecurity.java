package com.tpsolution.animestore.security;

import com.tpsolution.animestore.payload.AddUserRequest;
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
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    CustomOAuth2UserService oauthUserService;

    @Autowired
    ClientServiceImp userService;

    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000/"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST"));
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
         http.csrf(csrf -> csrf.disable())
                 .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/welcome", "/auth/generateToken","/auth/test-request","/auth/logout","/login**","/callback/", "/oauth2/authorization/**","/auth/logout-success","/auth/login-success").permitAll())
                 .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/user/**").authenticated())
                 .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/admin/**").authenticated())
                 .oauth2Login(oauth2LoginCustomizer -> oauth2LoginCustomizer
                         .userInfoEndpoint(userInfoEndpointCustomizer -> userInfoEndpointCustomizer.userService(oauthUserService))
                         .successHandler(new AuthenticationSuccessHandler() {
                             @Override
                             public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                 DefaultOidcUser defaultOidcUser = (DefaultOidcUser) authentication.getPrincipal();

                                 AddUserRequest req = getAddUserRequest(defaultOidcUser);
                                 userService.insertNewClient(req);

                                 // Redirect to a success page after user creation
                                 response.sendRedirect("/auth/login-success");
                             }
                         })
                 )
                 .logout((logout) -> logout.logoutSuccessUrl("/auth/logout-success"))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(customJwtFilter, UsernamePasswordAuthenticationFilter.class);
//        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
//                .httpBasic(withDefaults())
//                .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().permitAll())
//                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    private static AddUserRequest getAddUserRequest(DefaultOidcUser defaultOidcUser) {
        AddUserRequest req = new AddUserRequest();
        Set<Integer> roleId = new HashSet<>();
        roleId.add(6);
        req.setRoleId(roleId);
        req.setEmail(defaultOidcUser.getEmail());
        req.setPassword("Ab123456@");
        return req;
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