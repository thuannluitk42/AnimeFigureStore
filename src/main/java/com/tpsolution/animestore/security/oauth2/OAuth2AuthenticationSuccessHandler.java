package com.tpsolution.animestore.security.oauth2;

import com.tpsolution.animestore.entity.Roles;
import com.tpsolution.animestore.entity.Users;
import com.tpsolution.animestore.enums.Provider;
import com.tpsolution.animestore.repository.UsersRepository;
import com.tpsolution.animestore.security.CustomJwtFilter;
import com.tpsolution.animestore.security.JwtUtilsHelper;
import com.tpsolution.animestore.service.imp.RoleServiceImp;
import com.tpsolution.animestore.utils.CommonUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UsersRepository userService;
    private final RoleServiceImp roleService;
    private final JwtUtilsHelper jwtUtilsHelper;

    @Value("${frontend.url}")
    private String frontendUrl;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        if ("google".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())){
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String,Object> attributes = principal.getAttributes();
            String email = attributes.getOrDefault("email","").toString();
            String name = attributes.getOrDefault("name","").toString();
            String avatar = attributes.getOrDefault("picture","").toString();

            userService.findByEmail(email)
                    .ifPresentOrElse(user -> {
                        DefaultOAuth2User newUser = new DefaultOAuth2User(
                                listGrantedAuthority(user),
                                attributes,
                                "sub");
                        Authentication securityAuth = new OAuth2AuthenticationToken(newUser, listGrantedAuthority(user),
                                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                        SecurityContextHolder.getContext().setAuthentication(securityAuth);
                    }, () -> {
                        Users userEntity = new Users();

                        Set<Roles> setRolesRequest = new HashSet<>();
                        Roles roles = roleService.findByRoleId(6);
                        setRolesRequest.add(roles);
                        userEntity.setRoles(setRolesRequest);

                        userEntity.setEmail(email);
                        userEntity.setUsername(CommonUtils.extractUsernameFromEmail(email));
                        userEntity.setFullname(name);
                        userEntity.setProvider(Provider.GOOGLE);
                        userEntity.setPassword("Abc12345@");
                        userEntity.setAvatar(avatar);

                        String token = jwtUtilsHelper.generateToken(email);
                        userEntity.setToken(token);

                        userService.save(userEntity);

                        DefaultOAuth2User newUser = new DefaultOAuth2User(listGrantedAuthority(userEntity),
                                attributes, "sub");
                        Authentication securityAuth = new OAuth2AuthenticationToken(newUser, listGrantedAuthority(userEntity),
                                oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
                        SecurityContextHolder.getContext().setAuthentication(securityAuth);
                    });
        }

        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl(frontendUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private List<GrantedAuthority> listGrantedAuthority(Users userInfo) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        Iterator<Roles> iterator = userInfo.getRoles().iterator();
        List<String> roleName = new ArrayList<>();

        while (iterator.hasNext()) {
            Roles role = iterator.next();
            roleName.add(role.getRoleName());
        }

        for (String item:roleName) {
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(item);
            authorities.add(simpleGrantedAuthority);
        }
        return authorities;
    }
}


