package com.tpsolution.animestore.security.oauth2;

import com.tpsolution.animestore.entity.Roles;
import com.tpsolution.animestore.entity.Users;
import com.tpsolution.animestore.enums.Provider;
import com.tpsolution.animestore.enums.Role;
import com.tpsolution.animestore.repository.RolesRepository;
import com.tpsolution.animestore.repository.UsersRepository;
import com.tpsolution.animestore.security.UserInfoDetails;
import com.tpsolution.animestore.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UsersRepository userRepository;
    private final RolesRepository rolesRepository;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);
        return processOAuth2User(oAuth2UserRequest, oAuth2User);
    }

    protected OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        //OAuth2 login platform classification
        Provider authProvider = Provider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authProvider, oAuth2User.getAttributes());

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new RuntimeException("Email not found from OAuth2 provider");
        }

        Users user = userRepository.findByEmail(oAuth2UserInfo.getEmail()).orElse(null);
        //If you are already registered
        if (user != null) {
            if (!user.getProvider().equals(authProvider)) {
                throw new RuntimeException("Email already signed up.");
            }
            user = updateUser(user, oAuth2UserInfo);
        }
        //If you are not registered
        else {
            user = registerUser(authProvider, oAuth2UserInfo);
        }
        return UserInfoDetails.create(user, oAuth2UserInfo.getAttributes());
    }
    private Users registerUser(Provider authProvider, OAuth2UserInfo oAuth2UserInfo) {
        Set<Roles> setRolesRequest = new HashSet<>();
        Roles roles = rolesRepository.findByRoleName(Role.ROLE_CLIENT.name());
        setRolesRequest.add(roles);

        Users user = Users.builder()
                .email(oAuth2UserInfo.getEmail())
                .username(CommonUtils.extractUsernameFromEmail(oAuth2UserInfo.getEmail()) )
                .password("Abc12345@")
                .oauth2Id(oAuth2UserInfo.getOAuth2Id())
                .provider(authProvider)
                .roles(setRolesRequest)
                .isLogged(Boolean.TRUE)
                .build();

        return userRepository.save(user);
    }

    private Users updateUser(Users user, OAuth2UserInfo oAuth2UserInfo) {
        return userRepository.save(user.update(oAuth2UserInfo));
    }
}
