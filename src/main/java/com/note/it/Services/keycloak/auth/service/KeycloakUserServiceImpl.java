package com.note.it.Services.keycloak.auth.service;

import com.note.it.Entities.User;
import com.note.it.Repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KeycloakUserServiceImpl implements KeycloakUserService {

    private static final String USER_NOT_FOUND = "User Not Found";
    private static String fiuGroupId;
    private final RealmResource realmResource;

    @Autowired private UserRepository userRepository;

    @Value("${app.keycoak.user.role}")
    private String userRole;

    public KeycloakUserServiceImpl(final @Value("${app.keycloak.url}") String authServerUrl, final @Value("${app.keycloak.realm}") String realm, final @Value("${app.keycloak.client}") String clientId, final @Value("${app.keycloak.clientSecret}") String clientSecret) {

        realmResource = KeycloakBuilder.builder().serverUrl(authServerUrl).grantType(OAuth2Constants.CLIENT_CREDENTIALS).realm(realm).clientId(clientId).clientSecret(clientSecret).resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build().realm(realm);
    }

    @Override
    public boolean isUserExistWithEmailId(String emailId){
        return userRepository.countUserByEmailId(emailId) != 0;
    }

    @Override
    public boolean isUserVerifiedWithEmailId(String emailId) {
        User user = getUserByEmailId(emailId);

        return user.getIsVerified();
    }
}
