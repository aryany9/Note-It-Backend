package com.note.it.Controllers;

import com.note.it.Entities.UserSignUpRequest;
import com.note.it.Entities.UserSignUpResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api(tags = "User Authentication Flows APIs")
public interface UserAuthenticationApi {

    @ApiOperation("This API is intended for Signup Request")
    @RequestMapping(
            value = "/public/user/signup",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    ResponseEntity<UserSignUpResponse> signUp(@Validated @RequestBody UserSignUpRequest signUpRequest);
}
