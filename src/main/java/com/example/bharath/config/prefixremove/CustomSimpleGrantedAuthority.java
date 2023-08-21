package com.example.bharath.config.prefixremove;

import org.springframework.security.core.GrantedAuthority;

public class CustomSimpleGrantedAuthority implements GrantedAuthority {
    private final String authority;

    public CustomSimpleGrantedAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
