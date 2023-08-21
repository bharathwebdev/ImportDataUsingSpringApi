package com.example.bharath.config.securityconfig;



import com.example.bharath.config.prefixremove.CustomSimpleGrantedAuthority;
import com.example.bharath.model.BankUsers;
import com.example.bharath.repository.BankuserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BankAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    BankuserRepository bankuserRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username  = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        List<BankUsers> bankUsers = bankuserRepository.findByEmail(username);
        if(!bankUsers.isEmpty()){
            if(passwordEncoder.matches(pwd,bankUsers.get(0).getPassword())){
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new CustomSimpleGrantedAuthority(bankUsers.get(0).getRole()));
                return new UsernamePasswordAuthenticationToken(username,pwd,authorities);
            }else{
                throw  new BadCredentialsException("Invalid password");
            }
        } else{
            throw  new BadCredentialsException("User not found");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
