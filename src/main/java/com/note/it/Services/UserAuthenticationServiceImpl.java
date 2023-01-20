package com.note.it.Services;

import com.note.it.Entities.UserSignUpRequest;
import com.note.it.Entities.UserSignUpResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Slf4j
public class UserAuthenticationServiceImpl implements UserAuthenticationService{
    @Override
    public UserSignUpResponse signUp(UserSignUpRequest signUpRequest) {
        return null;
    }
}
