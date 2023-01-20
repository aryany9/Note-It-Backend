package com.note.it.Controllers;

import com.note.it.Services.UserAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/user")
@RestController
public class UserAuthenticationController implements UserAuthenticationApi{

    @Autowired
    private UserAuthenticationService userAuthenticationService;

    @PostMapping
}
