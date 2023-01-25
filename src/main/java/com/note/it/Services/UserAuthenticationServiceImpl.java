package com.note.it.Services;

import com.note.it.Entities.User;
import com.note.it.Entities.UserSignUpRequest;
import com.note.it.Entities.UserSignUpResponse;
import com.note.it.Exceptions.EntityFoundException;
import com.note.it.Exceptions.EntityNotFoundException;
import com.note.it.Services.keycloak.auth.service.KeycloakUserService;
import com.note.it.Utilities.HashingUtility;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@NoArgsConstructor
@Slf4j
public class UserAuthenticationServiceImpl implements UserAuthenticationService{
    @Autowired
    private KeycloakUserService keycloakUserService;
    @Override
    public UserSignUpResponse signUp(UserSignUpRequest signUpRequest) {
        if (keycloakUserService.isUserExistWithEmailId(signUpRequest.getEmailId())) {
            if (keycloakUserService.isUserVerifiedWithEmailId(signUpRequest.getEmailId())) {
                throw new EntityFoundException(
                        HttpStatus.BAD_REQUEST, "User already exists with this Email");
            }
            User user = keycloakUserService.getUserByEmailId(signUpRequest.getEmailId());
            keycloakUserService.deleteUnverifiedUser(user.getEmailId());
        }

        keycloakUserService.createUser(signUpRequest);
        UserSignUpResponse userSignUpResponse = verifyPassword();


        keycloakUserService.enableUser(signUpRequest.getEmailId());
        return userSignUpResponse;
    }


    @Override
    public UserSignUpResponse verifyPassword(
            final VerifyPinRequest verifyPinRequest, boolean shouldLogout, boolean isHashedPin) {

        String hashedPin;

        if (isHashedPin) {
            hashedPin = verifyPinRequest.getPin();
        } else {
            hashedPin =
                    HashingUtility.getHashedString(
                            verifyPinRequest.getPhoneNumber() + verifyPinRequest.getPin());
        }

        User user = null;
        try {
            user = keycloakUserService.getUserByPhoneNumber(verifyPinRequest.getPhoneNumber());
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException(
                    HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER);
        }
        if (shouldLogout) {
            if (!user.getPin().equals(hashedPin)) {
                throw new EntityNotFoundException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER);
            }
            return VerifyOtpResponse.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .phoneNumber(user.getPhoneNumber())
                    .vuaId(user.getVuaId())
                    .build();
        }
        final AuthenticateUserRequest authenticateUserRequest =
                AuthenticateUserRequest.builder()
                        .userName(user.getVuaId())
                        .clientSecret(pinClientSecret)
                        .clientId(pinClient)
                        .grantType(grantType)
                        .password(hashedPin)
                        .build();

        VerifyOtpResponse verifyOtpResponse = null;
        try {
            verifyOtpResponse = keycloakRestClientWithFormEncoder.verifyPassword(authenticateUserRequest);

            addMobileToken(verifyPinRequest.getMobileToken(), user);

            verifyOtpResponse.setFirstName(user.getFirstName());
            verifyOtpResponse.setLastName(user.getLastName());
            verifyOtpResponse.setVuaId(user.getVuaId());
            verifyOtpResponse.setPhoneNumber(verifyPinRequest.getPhoneNumber());
            return verifyOtpResponse;
        } catch (FeignException ex) {
            if (ex.status() == 401) {
                throw new EntityNotFoundException(
                        HttpStatus.UNAUTHORIZED, UNAUTHORIZED_USER);
            } else if (ex.status() == 400) {
                throw new AAException(HttpStatus.BAD_REQUEST, "User Account Disabled");
            } else {
                throw new ApiCommunicationException(
                        HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage());
            }
        }
    }
}
