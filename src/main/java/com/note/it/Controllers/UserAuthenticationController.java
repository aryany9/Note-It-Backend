package com.note.it.Controllers;

import com.note.it.Entities.UserSignUpRequest;
import com.note.it.Entities.UserSignUpResponse;
import com.note.it.Services.UserAuthenticationService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@NoArgsConstructor
public class UserAuthenticationController implements UserAuthenticationApi{

    @Autowired private UserAuthenticationService userAuthenticationService;

    @Override
    public ResponseEntity<UserSignUpResponse> signUp(final UserSignUpRequest userSignUpRequest){
        return ResponseEntity.ok(userAuthenticationService.signUp(userSignUpRequest));
    }
}
