package com.note.it.Services;

import com.note.it.Entities.UserSignUpRequest;
import com.note.it.Entities.UserSignUpResponse;
import org.springframework.stereotype.Service;


public interface UserAuthenticationService {

    UserSignUpResponse signUp(UserSignUpRequest signUpRequest);
}
