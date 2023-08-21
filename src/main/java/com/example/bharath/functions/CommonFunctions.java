package com.example.bharath.functions;

import com.example.bharath.dto.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class CommonFunctions {

    @Autowired
    AuthenticationManager authenticationManager;

    public Authentication AuthenticateUser(AuthRequest authRequest){
        Authentication authentication  =  authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );



        return authentication;
    }
}
