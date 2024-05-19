package com.tpsolution.animestore.security.oauth2;

import com.tpsolution.animestore.enums.Provider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(Provider authProvider, Map<String, Object> attributes) {
        switch (authProvider) {
            case GOOGLE: return new GoogleOAuth2User(attributes);
            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}
