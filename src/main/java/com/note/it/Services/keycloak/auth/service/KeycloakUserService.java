package com.note.it.Services.keycloak.auth.service;

import com.note.it.Entities.User;

public interface KeycloakUserService {

    User getUserByEmailId(String emailId);

    boolean isUserExistWithEmailId(final String EmailId);
    boolean isUserVerifiedWithEmailId(final String EmailId);

    User getUserFromToken();

    void deleteUser();

    void updateUser(User user);

    void addGroup(String entityId);

    void updatePassword(String emailId, String newPassword);

    void deleteUnverifiedUser(String emailId);
}
