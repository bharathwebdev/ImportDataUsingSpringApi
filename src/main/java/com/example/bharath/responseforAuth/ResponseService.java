package com.example.bharath.responseforAuth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ResponseService implements ResponseInterface {

    private  String issuer;
    private  String subject;
    private  String audience;
    private Date issuedAt;
    private  Date expirationTime;
    private  String jwtId;
    private Object role;
    private String bearerToken;
    private Object firstname;
    private Object lastname;

}
