package com.note.it.Services.keycloak.auth.service;

import com.note.it.Entities.User;
import com.note.it.Entities.UserSignUpRequest;
import com.note.it.Exceptions.EntityNotFoundException;
import com.note.it.Exceptions.NoteItException;
import com.note.it.Repositories.UserRepository;
import com.note.it.Utilities.HashingUtility;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

@Service
@Slf4j
public class KeycloakUserServiceImpl implements KeycloakUserService {

    private static final String USER_NOT_FOUND = "User Not Found";
    private static String fiuGroupId;
    private final RealmResource realmResource;

    @Autowired
    private UserRepository userRepository;

    @Value("${app.keycoak.user.role}")
    private String userRole;

    public KeycloakUserServiceImpl(final @Value("${app.keycloak.url}") String authServerUrl, final @Value("${app.keycloak.realm}") String realm, final @Value("${app.keycloak.client}") String clientId, final @Value("${app.keycloak.clientSecret}") String clientSecret) {

        realmResource = KeycloakBuilder.builder().serverUrl(authServerUrl).grantType(OAuth2Constants.CLIENT_CREDENTIALS).realm(realm).clientId(clientId).clientSecret(clientSecret).resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build()).build().realm(realm);
    }

    @Override
    public User getUserByEmailId(String emailId) {
        try {
            return userRepository.findByEmailId(emailId).get();
        } catch (NoSuchElementException ex) {
            throw new EntityNotFoundException(HttpStatus.NOT_FOUND, "user is not registered");
        }
    }

    @Override
    public String createUser(UserSignUpRequest signUpRequest) {
        // storing hash format of PIN in database (mobileNumber + pin)
        String hashedPin = HashingUtility.getHashedString(signUpRequest.getEmailId() + signUpRequest.getPassword());
        final UsersResource usersResource = realmResource.users();
        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setUsername(signUpRequest.getEmailId());
        userRepresentation.setEnabled(false);
        userRepresentation.setCredentials(Collections.singletonList(createPasswordCredentials(hashedPin)));
        usersResource.create(userRepresentation);
        User user = User.builder().firstName(signUpRequest.getFirstName()).lastName(signUpRequest.getLastName()).password(hashedPin).isVerified(false).build();
        userRepository.save(user);
        return signUpRequest.getEmailId();
    }

    @Override
    public boolean isUserExistWithEmailId(String emailId) {
        return userRepository.countUserByEmailId(emailId) != 0;
    }

    @Override
    public boolean isUserVerifiedWithEmailId(String emailId) {
        User user = getUserByEmailId(emailId);

        return user.getIsVerified();
    }

    @Override
    public User getUserFromToken() {
        KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            return userRepository.findByEmailId(keycloakPrincipal.getKeycloakSecurityContext().getToken().getPreferredUsername()).get();
        } catch (NoSuchElementException notFoundException) {
            throw new EntityNotFoundException(HttpStatus.NOT_FOUND, USER_NOT_FOUND);
        }
    }

    public CredentialRepresentation createPasswordCredentials(final String password) {
        final CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

    @Override
    public void deleteUser() {
        KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Response userDeleteResponse = realmResource.users().delete(keycloakPrincipal.getName());

        if (userDeleteResponse.getStatusInfo() == Response.Status.NOT_FOUND) {
            throw new EntityNotFoundException(HttpStatus.NOT_FOUND, USER_NOT_FOUND);
        } else if (userDeleteResponse.getStatusInfo() == Response.Status.INTERNAL_SERVER_ERROR) {
            throw new NoteItException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
        }
        User user = getUser(keycloakPrincipal.getKeycloakSecurityContext().getToken().getPreferredUsername(), () -> new EntityNotFoundException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
        userRepository.delete(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void addGroup(String entityId) {

    }

    @Override
    public User getUser(final String emailId, final Supplier<RuntimeException> userNotFoundExceptionSupplier) {
        try {
            return userRepository.findByEmailId(emailId).get();
        } catch (NoSuchElementException ex) {
            throw userNotFoundExceptionSupplier.get();
        }
    }

    @Override
    public void updatePassword(String emailId, String newPassword) {

    }

    @Override
    public void deleteUnverifiedUser(String emailId) {
        List<UserRepresentation> userRepresentationsList = realmResource.users().search(emailId);
        if (userRepresentationsList.isEmpty()) return;
        Response userDeleteResponse = realmResource.users().delete(userRepresentationsList.get(0).getId());

        if (userDeleteResponse.getStatusInfo() == Response.Status.NOT_FOUND) {
            throw new EntityNotFoundException(HttpStatus.NOT_FOUND, USER_NOT_FOUND);
        } else if (userDeleteResponse.getStatusInfo() == Response.Status.INTERNAL_SERVER_ERROR) {
            throw new NoteItException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
        }
        userRepository.delete(getUser(emailId, () -> new EntityNotFoundException(HttpStatus.NOT_FOUND, USER_NOT_FOUND)));

    }

    @Override
    public void enableUser(final String vuaId) {
        List<UserRepresentation> userRepresentationsList = realmResource.users().search(vuaId);
        if (userRepresentationsList.isEmpty()) return;
        UserRepresentation userRepresentation = userRepresentationsList.get(0);
        userRepresentation.setEnabled(true);
        final UserResource userResource = realmResource.users().get(userRepresentation.getId());

        userResource.roles().realmLevel().add(Collections.singletonList(realmResource.roles().get(userRole).toRepresentation()));
        userResource.update(userRepresentation);

        User user = getUser(userRepresentation.getUsername(), () -> new EntityNotFoundException(HttpStatus.NOT_FOUND, USER_NOT_FOUND));
        user.setIsVerified(true);
        updateUser(user);
    }
}
