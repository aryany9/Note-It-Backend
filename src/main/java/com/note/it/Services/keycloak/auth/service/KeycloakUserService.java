package com.note.it.Services.keycloak.auth.service;

public interface KeycloakUserService {

    boolean isUserExistWithEmailId(final String EmailId);
    boolean isUserVerifiedWithEmailId(final String EmailId);
}
