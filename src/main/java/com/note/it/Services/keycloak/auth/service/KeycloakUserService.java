package com.note.it.Services.keycloak.auth.service;

import com.note.it.Entities.User;
import com.note.it.Entities.UserSignUpRequest;

import java.util.function.Supplier;

public interface KeycloakUserService {

    User getUserByEmailId(String emailId);
    String createUser(UserSignUpRequest signUpRequest);

    void enableUser(String vuaId);
    boolean isUserExistWithEmailId(final String EmailId);
    boolean isUserVerifiedWithEmailId(final String EmailId);

    User getUser(
            final String vuaId, final Supplier<RuntimeException> userNotFoundExceptionSupplier);

    User getUserFromToken();

    void deleteUser();

    void updateUser(User user);

    void addGroup(String entityId);

    void updatePassword(String emailId, String newPassword);

    void deleteUnverifiedUser(String emailId);

}
